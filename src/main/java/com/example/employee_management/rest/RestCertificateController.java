package com.example.employee_management.rest;

import com.globits.da.dto.CertificateDto;
import com.globits.da.dto.EmployeeCertificateDto;
import com.globits.da.exception.ObjectFieldNotValidException;
import com.globits.da.service.CertificateService;
import com.globits.da.validation.condition.OnCreate;
import com.globits.da.validation.condition.OnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/certificate")
public class RestCertificateController {
    private final CertificateService certificateService;

    @Autowired
    public RestCertificateController(CertificateService certificateService){
        this.certificateService = certificateService;
    }

    @RequestMapping()
    public List<CertificateDto> findAll(){
        return this.certificateService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findOneById(@PathVariable UUID id){
        return ResponseEntity.ok(this.certificateService.findOneById(id));
    }

    @PostMapping()
    public ResponseEntity<Object> save(@Validated(OnCreate.class) @RequestBody CertificateDto certificateDto, BindingResult result){
        if(result.hasErrors()){
            throw new ObjectFieldNotValidException(result.getFieldErrors());
        }

        CertificateDto saveCertificate = this.certificateService.saveCertificate(certificateDto);
        return ResponseEntity.ok(saveCertificate);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable UUID id ,@Validated(OnUpdate.class) @RequestBody CertificateDto certificateDto, BindingResult result){
        if(result.hasErrors()){
            throw new ObjectFieldNotValidException(result.getFieldErrors());
        }
        certificateDto.setId(id);
        CertificateDto updateCerticate = this.certificateService.updateCertificate(certificateDto);
        return ResponseEntity.ok(updateCerticate);
    }

    @PostMapping("/give-certificate-to-employee")
    public ResponseEntity<Object> giveCertificateToEmployee(@Validated(OnCreate.class) @RequestBody EmployeeCertificateDto certificateDto){
        EmployeeCertificateDto giveCertificates = this.certificateService.giveCertificatesToEmployee(certificateDto);
        return ResponseEntity.ok(giveCertificates);
    }
}

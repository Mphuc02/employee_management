package com.example.employee_management.rest;

import com.globits.da.constant.Constant.*;
import com.globits.da.constant.ErrorMessage.*;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.dto.search.SearchProvinceDto;
import com.globits.da.exception.ObjectFieldNotValidException;
import com.globits.da.service.ProvinceService;
import com.globits.da.validation.condition.OnCreate;
import com.globits.da.validation.condition.OnDefault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/province")
public class RestProvinceController {
    private final ProvinceService provinceService;

    @Autowired
    public RestProvinceController(@Qualifier(BEAN_SERVICE.PROVINCE_SERVICE) ProvinceService provinceService){
        this.provinceService = provinceService;
    }

    @GetMapping()
    public ResponseEntity<List<ProvinceDto>> findAllProvinces(){
        return ResponseEntity.ok(provinceService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") UUID id){
        ProvinceDto result = provinceService.findById(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/find")
    public SearchProvinceDto getByCondition(@RequestBody SearchProvinceDto searchDto){
        return provinceService.findByCondition(searchDto);
    }

    @PostMapping()
    public ResponseEntity<Object> addProvince(@Validated({OnCreate.class, OnDefault.class}) @RequestBody ProvinceDto provinceDto, BindingResult result){
        if(result.hasErrors()){
            throw new ObjectFieldNotValidException(result.getFieldErrors());
        }
        ProvinceDto saveProvince = provinceService.saveProvince(provinceDto);
        return ResponseEntity.ok(saveProvince);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProvince(@PathVariable UUID id,@Validated({OnDefault.class}) @RequestBody ProvinceDto provinceDto, BindingResult result){
        if(result.hasErrors()){
            throw new ObjectFieldNotValidException(result.getFieldErrors());
        }
        provinceDto.setId(id);
        provinceDto = provinceService.updateProvince(provinceDto);
        return ResponseEntity.ok(provinceDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProvince(@PathVariable UUID id){
        this.provinceService.delete(id);
        return ResponseEntity.ok(String.format(PROVINCE_ERRORS.DELETE_SUCCESS, id));
    }
}

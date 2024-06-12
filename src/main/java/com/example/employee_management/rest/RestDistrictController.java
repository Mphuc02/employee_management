package com.example.employee_management.rest;

import com.globits.da.constant.Constant.*;
import com.globits.da.constant.ErrorMessage;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.search.SearchDistrictDto;
import com.globits.da.exception.ObjectFieldNotValidException;
import com.globits.da.service.DistrictService;
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
@RequestMapping("/district")
public class RestDistrictController {
    private final DistrictService districtService;

    @Autowired
    public RestDistrictController(@Qualifier(BEAN_SERVICE.DISTRICT_SERVICE) DistrictService districtService){
        this.districtService = districtService;
    }

    @GetMapping()
    public List<DistrictDto> getAllDistricts(){
        return this.districtService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findOneById(@PathVariable UUID id){
        DistrictDto findDistrict = this.districtService.findById(id);
        return ResponseEntity.ok(findDistrict);
    }

    @PostMapping("/find")
    public SearchDistrictDto findByCondition(@RequestBody SearchDistrictDto searchDistrictDto){
        return this.districtService.findAllByCondition(searchDistrictDto);
    }

    @PostMapping()
    public ResponseEntity<Object> addDistrict(@Validated({OnCreate.class, OnDefault.class}) @RequestBody DistrictDto districtDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new ObjectFieldNotValidException(bindingResult.getFieldErrors());
        }

        DistrictDto saveDistrictResult = this.districtService.saveDistrict(districtDto);
        return ResponseEntity.ok().body(saveDistrictResult);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateDistrict(@PathVariable UUID id ,@Validated(OnDefault.class) @RequestBody DistrictDto districtDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new ObjectFieldNotValidException(bindingResult.getFieldErrors());
        }
        districtDto.setId(id);
        DistrictDto updateDistrictResult = this.districtService.updateDistrict(districtDto);
        return ResponseEntity.ok().body(updateDistrictResult);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteDistrict(@PathVariable UUID id){
        this.districtService.deleteDistrict(id);
        String deleteSuccess = String.format(ErrorMessage.DISTRICT_ERRORS.DELETE_SUCCESS, id);
        return ResponseEntity.ok(deleteSuccess);
    }
}

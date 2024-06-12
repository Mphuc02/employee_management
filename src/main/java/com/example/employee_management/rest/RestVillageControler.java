package com.example.employee_management.rest;

import com.globits.da.constant.Constant.*;
import com.globits.da.constant.ErrorMessage.*;
import com.globits.da.dto.VillageDto;
import com.globits.da.dto.search.SearchVillageDto;
import com.globits.da.exception.ObjectFieldNotValidException;
import com.globits.da.service.VillageService;
import com.globits.da.validation.condition.OnCreate;
import com.globits.da.validation.condition.OnDefault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("village")
public class RestVillageControler {
    private final VillageService villageService;

    @Autowired
    public RestVillageControler(@Qualifier(BEAN_SERVICE.VILLAGE_SERVICE) VillageService villageService){
        this.villageService = villageService;
    }

    @GetMapping()
    public List<VillageDto> getAllVillages(){
        return this.villageService.findAllVillages();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findOneById(@PathVariable UUID id){
        return ResponseEntity.ok(this.villageService.findOneById(id));
    }

    @PostMapping("/find")
    public SearchVillageDto findByCondition(@RequestBody SearchVillageDto searchDto){
        return this.villageService.findByCondition(searchDto);
    }

    @PostMapping("")
    public ResponseEntity<Object> saveVillage(@Validated({OnCreate.class, OnDefault.class}) @RequestBody VillageDto villageDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new ObjectFieldNotValidException(bindingResult.getFieldErrors());
        }

        VillageDto saveVillage = this.villageService.saveVillage(villageDto);
        return ResponseEntity.ok(saveVillage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateVillage(@PathVariable UUID id ,@Validated(OnDefault.class) @RequestBody VillageDto villageDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new ObjectFieldNotValidException(bindingResult.getFieldErrors());
        }

        villageDto.setId(id);
        VillageDto updateVillage = this.villageService.updateVillage(villageDto);
        return ResponseEntity.ok(updateVillage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteVillage(@PathVariable UUID id){
        this.villageService.deleteVillage(id);
        String deleteSuccess = String.format(VILLAGE_ERRORS.DELETE_SUCCESS, id);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(deleteSuccess );
    }
}

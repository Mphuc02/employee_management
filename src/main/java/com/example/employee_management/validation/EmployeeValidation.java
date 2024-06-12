package com.example.employee_management.validation;

import com.globits.da.constant.Constant.*;
import com.globits.da.constant.ErrorMessage.*;
import com.globits.da.domain.Employee;
import com.globits.da.dto.*;
import com.globits.da.exception.DuplicateException;
import com.globits.da.exception.NotFoundException;
import com.globits.da.exception.NotNullException;
import com.globits.da.service.DistrictService;
import com.globits.da.service.EmployeeService;
import com.globits.da.service.VillageService;
import com.globits.da.utils.ReFormatNameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.UUID;

@Component
public class EmployeeValidation {
    private final EmployeeService employeeService;
    private final ReFormatNameUtils reFormatNameUtils;
    private final DistrictService districtService;
    private final VillageService villageService;
    private final ProvinceValidation provinceValidation;
    private final DistrictValidation districtValidation;
    private final VillageValidation villageValidation;
    private Integer conditionCheck;

    @Autowired
    @Lazy
    public EmployeeValidation(@Qualifier(BEAN_SERVICE.EMPLOYEE_SERVICE) EmployeeService employeeService,
                              @Qualifier(BEAN_SERVICE.DISTRICT_SERVICE) DistrictService districtService,
                              @Qualifier(BEAN_SERVICE.VILLAGE_SERVICE) VillageService villageService,
                              ProvinceValidation provinceValidation,
                              DistrictValidation districtValidation,
                              VillageValidation villageValidation,
                              ReFormatNameUtils reFormatNameUtils){
        this.employeeService = employeeService;
        this.reFormatNameUtils = reFormatNameUtils;
        this.districtService = districtService;
        this.villageService = villageService;
        this.provinceValidation = provinceValidation;
        this.districtValidation = districtValidation;
        this.villageValidation = villageValidation;
    }

    public void checkValid(EmployeeDto validEmployee, int conditionCheck, Employee entitySetvalue){
        this.conditionCheck = conditionCheck;

        //Kiểm tra các thuộc tính
        this.checkCode(validEmployee.getCode());
        this.checkName(validEmployee);
        this.checkEmail(validEmployee.getEmail());
        this.checkAddress(validEmployee.getProvince(), validEmployee.getDistrict(), validEmployee.getVillage());

        if(!ObjectUtils.isEmpty(entitySetvalue)){
            if(!ObjectUtils.isEmpty(validEmployee.getId()))
                entitySetvalue.setId(validEmployee.getId());
            if(!ObjectUtils.isEmpty(validEmployee.getCode()))
                entitySetvalue.setCode(validEmployee.getCode());
            if(!ObjectUtils.isEmpty(validEmployee.getName()))
                entitySetvalue.setName(validEmployee.getName());
            if(!ObjectUtils.isEmpty(validEmployee.getEmail()))
                entitySetvalue.setEmail(validEmployee.getEmail());

            if(!ObjectUtils.isEmpty(validEmployee.getProvince()))
                entitySetvalue.getProvince().setId(validEmployee.getProvince().getId());

            if(!ObjectUtils.isEmpty(validEmployee.getDistrict()))
                entitySetvalue.getDistrict().setId(validEmployee.getDistrict().getId());

            if(!ObjectUtils.isEmpty(validEmployee.getVillage()))
                entitySetvalue.getVillage().setId(validEmployee.getVillage().getId());
        }
    }

    public void checkId(UUID id){
        if(this.conditionCheck == VALIDATION.CREATE)
            return;

        if(ObjectUtils.isEmpty(id))
            throw new NotNullException(EMPLOYEE_ERRORS.ID_NOT_NULL);

        if(!this.employeeService.checkIdExisted(id)){
            String idNotFound = String.format(EMPLOYEE_ERRORS.ID_NOT_FOUND, id);
            throw new NotFoundException(idNotFound);
        }
    }

    public void checkId(EmployeeDto checkId){
        if(ObjectUtils.isEmpty(checkId))
            throw new NotNullException(EMPLOYEE_ERRORS.ID_NOT_NULL);
        this.conditionCheck = VALIDATION.UPDATE;
        //gọi đến hàm checkId có 1 tham số
        this.checkId(checkId.getId());
        this.conditionCheck = null;
    }

    public void checkCode(String code){
        //Kiểm tra xem đã tồn tại code này hay chưa
        if(!ObjectUtils.isEmpty(code)){
            //Khi tạo mới 1 employee thì kiểm tra xem id này có tồn tại hay không
            if(this.conditionCheck == VALIDATION.CREATE && this.employeeService.checkCodeExisted(code)){
                String codeExisted = String.format(EMPLOYEE_ERRORS.CODE_EXISTED, code);
                throw new DuplicateException(codeExisted);
            }
        }
    }

    public void checkName(EmployeeDto validEmployee){
        String name = validEmployee.getName();
        if(!ObjectUtils.isEmpty(name)){
            //Chuẩn hóa lại tên
            name = this.reFormatNameUtils.reFormatName(name);
            validEmployee.setName(name);
        }
    }

    public void checkEmail(String email){
        if(!ObjectUtils.isEmpty(email)){
            //Kiểm tra xem có người nào đã đăng ký email này chưa
            if(this.employeeService.checkEmailExisted(email)){
                String emailExisted = String.format(EMPLOYEE_ERRORS.EMAIL_EXISTED, email);
                throw new DuplicateException(emailExisted);
            }
        }
    }

    public void checkAddress(ProvinceDto province,
                             DistrictDto district,
                             VillageDto village){

        if(!ObjectUtils.isEmpty(province) || !ObjectUtils.isEmpty(district) || !ObjectUtils.isEmpty(village)){
            //Kiểm tra Province và id của nó khác null
            this.provinceValidation.checkId(province);
            //Kiểm tra District và id của nó khác null
            this.districtValidation.checkId(district);
            //Kiểm tra village và id của nó khác null
            this.villageValidation.checkId(village);

            //Kiểm tra tỉnh có huyện này không
            if(!this.districtService.checkProvinceHasDistrict(province.getId(), district.getId())){
                String provinceNotOwnDistrict = String.format(PROVINCE_ERRORS.PROVINCE_NOT_OWN_DISTRICT, district.getId(), province.getId());
                throw new NotFoundException(provinceNotOwnDistrict);
            }
            //Kiểm tra huyện có làng này không
            if(!this.villageService.checkDistrictHasVillage(district.getId(), village.getId())){
                String districtNotOwnVillage = String.format(DISTRICT_ERRORS.DISTRICT_NOT_OWN_VILLAGE, village.getId(), district.getId());
                throw new NotFoundException(districtNotOwnVillage);
            }
        }
    }
}

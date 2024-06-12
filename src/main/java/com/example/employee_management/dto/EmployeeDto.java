package com.example.employee_management.dto;

import com.example.employee_management.constant.ErrorMessage.*;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.validation.condition.OnCreate;
import com.example.employee_management.validation.condition.OnDefault;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.ObjectUtils;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeDto extends BaseObjectDTO {
    @NotNull(groups = OnCreate.class, message = EMPLOYEE_ERRORS.CODE_NOT_NULL)
    @Length(groups = OnDefault.class ,min = 6, max = 10, message = EMPLOYEE_ERRORS.CODE_LENGTH)
    private String code;

    @NotNull(groups = OnCreate.class, message = EMPLOYEE_ERRORS.NAME_NOT_NULL)
    @Length(groups = OnDefault.class ,min = 1, max = 50, message = EMPLOYEE_ERRORS.NAME_LENGTH)
    private String name;

    @NotNull(groups = OnCreate.class, message = EMPLOYEE_ERRORS.EMAIL_NOT_NULL)
    @Length(groups = OnDefault.class ,min = 1, max = 50, message = EMPLOYEE_ERRORS.EMAIL_LENGTH)
    @Email(groups = OnDefault.class ,message = EMPLOYEE_ERRORS.EMAIL_NOT_FORMAT)
    private String email;

    @NotNull(groups = OnCreate.class, message = EMPLOYEE_ERRORS.PHONE_NOT_NULL)
    @Length(groups = OnDefault.class ,min = 1, max = 11, message = EMPLOYEE_ERRORS.PHONE_LENGTH)
    private String phone;

    @NotNull(groups = OnCreate.class, message = EMPLOYEE_ERRORS.AGE_NOT_NULL)
    @Min(groups = OnDefault.class ,value = 1, message = EMPLOYEE_ERRORS.AGE_MIN)
    @Max(groups = OnDefault.class ,value = 150, message = EMPLOYEE_ERRORS.AGE_MAX)
    private Integer age;

    @NotNull(groups = OnCreate.class, message = PROVINCE_ERRORS.ID_NOT_NULL)
    private ProvinceDto province;

    @NotNull(groups =  OnCreate.class, message = DISTRICT_ERRORS.ID_NOT_NULL)
    private DistrictDto district;

    @NotNull(groups = OnCreate.class, message = VILLAGE_ERRORS.ID_NOT_NULL)
    private VillageDto village;

    private String[] message;
    private List<EmployeeCertificateDto> certificates;

    public EmployeeDto(){}

    public EmployeeDto(Employee entity){
        if(!ObjectUtils.isEmpty(entity)){
            this.setId(entity.getId());
            this.code = entity.getCode();
            this.name = entity.getName();
            this.email = entity.getEmail();
            this.phone = entity.getPhone();
            this.age = entity.getAge();

            if(!ObjectUtils.isEmpty(entity.getProvince())){
                this.province = new ProvinceDto(entity.getProvince());
            }

            if(!ObjectUtils.isEmpty(entity.getDistrict())){
                this.district = new DistrictDto(entity.getDistrict());
            }

            if(!ObjectUtils.isEmpty(entity.getVillage())){
                this.village = new VillageDto(entity.getVillage());
            }

            if(!ObjectUtils.isEmpty(entity.getCertificates())){
                this.certificates = entity.getCertificates().stream().map(EmployeeCertificateDto::new).collect(Collectors.toList());
            }
        }
    }
}

package com.example.employee_management.dto;

import com.example.employee_management.constant.ErrorMessage.*;
import com.example.employee_management.entity.Certificate;
import com.example.employee_management.entity.EmployeeCertificate;
import com.example.employee_management.validation.condition.OnCreate;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.ObjectUtils;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CertificateDto extends BaseObjectDTO{
    @NotNull(groups = OnCreate.class, message = CERTIFICATE_ERRORS.CERTIFICATE_NAME_NOT_NULL)
    private String name;

    public CertificateDto(){}

    public CertificateDto(Certificate entity){
        if(!ObjectUtils.isEmpty(entity)){
            this.setId(entity.getId());
            this.name = entity.getName();
        }
    }

    private List<EmployeeCertificate> employees;
}
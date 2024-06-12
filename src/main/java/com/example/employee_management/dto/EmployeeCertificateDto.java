package com.example.employee_management.dto;

import com.example.employee_management.constant.ErrorMessage.*;
import com.example.employee_management.entity.EmployeeCertificate;
import com.example.employee_management.validation.condition.OnCreate;
import com.example.employee_management.validation.condition.OnDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.ObjectUtils;
import java.sql.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeCertificateDto extends BaseObjectDTO {
    @NotNull(groups = OnDefault.class, message = CERTIFICATE_ERRORS.EMPLOYEE_ID_NOT_NULL)
    private EmployeeDto employee;
    @NotNull(groups = OnCreate.class, message = CERTIFICATE_ERRORS.CERTIFICATE_ID_NOT_NULL)
    private CertificateDto certificate;
    @NotNull(groups = OnDefault.class, message = CERTIFICATE_ERRORS.PROVINCE_ID_NOT_NULL)
    private ProvinceDto province;
    @NotNull(groups = OnCreate.class, message = CERTIFICATE_ERRORS.VALID_DATE_NOT_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy") //định dạng của ngày được gửi trong json
    private Date validDate;
    @NotNull(groups = OnCreate.class, message = CERTIFICATE_ERRORS.EXPIRE_DATE_NOT_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date expireDate;

    public EmployeeCertificateDto(){}
    public EmployeeCertificateDto(EmployeeCertificate entity){
        if(!ObjectUtils.isEmpty(entity)){
            validDate = entity.getValidDate();
            expireDate = entity.getExpireDate();
        }
    }
}

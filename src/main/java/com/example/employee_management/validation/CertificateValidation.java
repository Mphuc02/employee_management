package com.example.employee_management.validation;

import com.globits.da.constant.Constant.*;
import com.globits.da.constant.ErrorMessage.*;
import com.globits.da.domain.Certificate;
import com.globits.da.domain.Employee;
import com.globits.da.domain.EmployeeCertificate;
import com.globits.da.domain.Province;
import com.globits.da.dto.CertificateDto;
import com.globits.da.dto.EmployeeCertificateDto;
import com.globits.da.exception.DuplicateException;
import com.globits.da.exception.NotFoundException;
import com.globits.da.exception.NotNullException;
import com.globits.da.repository.CertificateRepository;
import com.globits.da.repository.EmployeeCertificateRepository;
import com.globits.da.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.UUID;

@Component
public class CertificateValidation {
    private final CertificateService certificateService;
    private final ProvinceValidation provinceValidation;
    private final EmployeeValidation employeeValidation;
    private final CertificateRepository certificateRepository;
    private final EmployeeCertificateRepository employeeCertificateRepository;
    private Integer condition;

    @Autowired
    @Lazy
    public CertificateValidation(CertificateService certificateService,
                                 ProvinceValidation provinceValidation,
                                 EmployeeValidation employeeValidation,
                                 CertificateRepository certificateRepository,
                                 EmployeeCertificateRepository employeeCertificateRepository){
        this.certificateService = certificateService;
        this.provinceValidation = provinceValidation;
        this.employeeValidation = employeeValidation;
        this.certificateRepository = certificateRepository;
        this.employeeCertificateRepository = employeeCertificateRepository;
    }

    public EmployeeCertificate checkEmployeeCanGetCertificate(EmployeeCertificateDto validCertificate, int conditionCheck){
        UUID employeeId = validCertificate.getEmployee().getId();
        UUID provinceId = validCertificate.getProvince().getId();
        UUID certificateId = validCertificate.getCertificate().getId();

        //Kiểm tra các dữ liệu
        this.condition = conditionCheck;
        EmployeeCertificate setValueEntity = new EmployeeCertificate();
        if(this.condition == VALIDATION.UPDATE){
            setValueEntity = this.employeeCertificateRepository.findById(validCertificate.getId()).orElseThrow( () -> {
                String idNotFound = String.format(CERTIFICATE_ERRORS.ID_EMPLOYEE_CERTIFICATE_NOT_FOUND, validCertificate.getId());
                return new NotFoundException(idNotFound);
            });
        }

        this.checkCertificateId(validCertificate.getCertificate().getId());
        this.provinceValidation.checkId(validCertificate.getProvince());
        this.employeeValidation.checkId(validCertificate.getEmployee());

        //Kiểm tra xem certificate của tỉnh này được trao cho employee đã hết hạn hay chưa
        if(!this.certificateService.checkCertificateOfEmployeeValid(employeeId, provinceId, certificateId)){
            String certificateDuplicate = String.format(CERTIFICATE_ERRORS.CERTIFICATE_OF_PROVINCE_STILL_VALID, certificateId);
            throw new DuplicateException(certificateDuplicate);
        }
        //Kiểm tra xem số lượng certificate cùng loại khác tỉnh của employee này có vượt quá 3 không
        if(this.certificateService.countCertificateOfEmployeeValidById(employeeId, certificateId) >= EMPLOYEE.MAX_CERTIFICATE_VALID_OF_EMPLOYEE){
            String certificateDuplicate = String.format(EMPLOYEE_ERRORS.EMPLOYEE_HAS_TOO_MUCH_CERTIFICATE, certificateId, EMPLOYEE.MAX_CERTIFICATE_VALID_OF_EMPLOYEE);
            throw new DuplicateException(certificateDuplicate);
        }

        if(!ObjectUtils.isEmpty(validCertificate.getCertificate())){
            Certificate certificate = new Certificate();
            certificate.setId(validCertificate.getCertificate().getId());
            setValueEntity.setCertificate(certificate);
        }
        if (!ObjectUtils.isEmpty(validCertificate.getProvince())) {
            Province province = new Province();
            province.setId(validCertificate.getProvince().getId());
            setValueEntity.setProvince(province);
        }
        if(!ObjectUtils.isEmpty(validCertificate.getEmployee())){
            Employee employee = new Employee();
            employee.setId(validCertificate.getEmployee().getId());
            setValueEntity.setEmployee(employee);
        }
        if(!ObjectUtils.isEmpty(validCertificate.getValidDate())){
            setValueEntity.setValidDate(validCertificate.getValidDate());
        }
        if(!ObjectUtils.isEmpty(validCertificate.getExpireDate())){
            setValueEntity.setExpireDate(validCertificate.getExpireDate());
        }

        return setValueEntity;
    }

    private void checkCertificateId(UUID id){
        if(ObjectUtils.isEmpty(id))
            throw new NotNullException(CERTIFICATE_ERRORS.CERTIFICATE_ID_NOT_NULL);

        if(this.certificateService.checkExistById(id)) {
            if (this.condition == VALIDATION.UPDATE) {
                String idNotFound = String.format(CERTIFICATE_ERRORS.CERTIFICATE_ID_NOT_FOUND, id);
                throw new NotFoundException(idNotFound);
            }
        }
    }

    private void checkEmployeeCertificateId(UUID id){
        if(ObjectUtils.isEmpty(id))
            throw new NotNullException(CERTIFICATE_ERRORS.ID_EMPLOYEE_CERTIFICATE_NOT_NULL);

        if(this.certificateService.checkEmployeeCertificateExistById(id)){
            if(this.condition == VALIDATION.CREATE){
                String idExisted = String.format(CERTIFICATE_ERRORS.ID_EMPLOYEE_CERTIFICATE_EXISTED, id);
                throw new DuplicateException(idExisted);
            }
        }
        else {
            if(this.condition == VALIDATION.UPDATE){
                String idNotFound = String.format(CERTIFICATE_ERRORS.ID_EMPLOYEE_CERTIFICATE_NOT_FOUND, id);
                throw new NotFoundException(idNotFound);
            }
        }
    }

    public Certificate checkCertificate(CertificateDto certificateDto, int condition){
        this.condition = condition;
        //Kiểm tra thông tin của CertificateDto bên trong EmployeeCertificateDto
        Certificate entitySetValue = new Certificate();
        if(this.condition == VALIDATION.UPDATE){
            entitySetValue = this.certificateRepository.findById(certificateDto.getId()).orElseThrow( () -> {
                String idNotFound = String.format(CERTIFICATE_ERRORS.CERTIFICATE_ID_NOT_FOUND, certificateDto.getId());
                return new NotFoundException(idNotFound);
            });
        }
        this.checkCertificateId(entitySetValue.getId());
        this.checkName(certificateDto.getName(), entitySetValue.getId());

        if(!ObjectUtils.isEmpty(certificateDto.getName())){
            entitySetValue.setName(certificateDto.getName());
        }

        return entitySetValue;
    }

    private void checkName(String name, UUID id){
        //Kiểm tra xem tỉnh này đã có certificate nào có tên như này chưa
        if(this.certificateService.checkExistByIdAndName(id, name)){
            String nameExisted = String.format(CERTIFICATE_ERRORS.CERTIFICATE_NAME_EXISTED, name);
            throw new DuplicateException(nameExisted);
        }
    }
}
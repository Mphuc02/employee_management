package com.example.employee_management.service.impl;

import com.globits.da.constant.Constant.*;
import com.globits.da.constant.ErrorMessage.*;
import com.globits.da.domain.Certificate;
import com.globits.da.domain.EmployeeCertificate;
import com.globits.da.dto.CertificateDto;
import com.globits.da.dto.EmployeeCertificateDto;
import com.globits.da.exception.NotFoundException;
import com.globits.da.repository.CertificateRepository;
import com.globits.da.repository.EmployeeCertificateRepository;
import com.globits.da.service.CertificateService;
import com.globits.da.validation.CertificateValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service(value = BEAN_SERVICE.CERTIFICATE_SERVICE)
public class CertificateServiceImpl implements CertificateService {
    private final CertificateRepository certificateRepository;
    private final EmployeeCertificateRepository employeeCertificateRepository;
    private final CertificateValidation certificateValidation;

    @Autowired
    @Lazy
    public CertificateServiceImpl(CertificateRepository certificateRepository,
                                  CertificateValidation certificateValidation,
                                  EmployeeCertificateRepository employeeCertificateRepository){
        this.certificateRepository = certificateRepository;
        this.certificateValidation = certificateValidation;
        this.employeeCertificateRepository = employeeCertificateRepository;
    }

    @Override
    public List<CertificateDto> findAll() {
        return this.certificateRepository.findAllDtos();
    }

    @Override
    public CertificateDto findOneById(UUID id) {
        return new CertificateDto(this.findCertificateEntityById(id));
    }

    @Override
    public CertificateDto saveCertificate(CertificateDto saveCertificateDto) {
        //Kiểm tra tính hợp lệ của các dữ liệu
        Certificate saveCertificate = this.certificateValidation.checkCertificate(saveCertificateDto, VALIDATION.CREATE);
        saveCertificate = this.certificateRepository.save(saveCertificate);
        return new CertificateDto(saveCertificate);
    }

    @Override
    public CertificateDto updateCertificate(CertificateDto updateCertificateDto) {
        //Kiểm tra tinh hợp lệ của dữ liệu
        Certificate updateCertificate = this.certificateValidation.checkCertificate(updateCertificateDto, VALIDATION.UPDATE);
        //Tiến hành lưu
        updateCertificate = this.certificateRepository.save(updateCertificate);
        //Trả về toàn bộ thông tin được update
        return new CertificateDto(updateCertificate);
    }

    @Override
    public EmployeeCertificateDto giveCertificatesToEmployee(EmployeeCertificateDto employeeCertificateDto) {
        //Kiểm tra các dữ liệu
        EmployeeCertificate save = this.certificateValidation.checkEmployeeCanGetCertificate(employeeCertificateDto, VALIDATION.CREATE);
        save = this.employeeCertificateRepository.save(save);
        return new EmployeeCertificateDto(save);
    }

    @Override
    public boolean checkCertificateOfEmployeeValid(UUID employeeId, UUID provinceId, UUID certificateId) {
        //đúng nếu employee chưa có certificate thuộc tỉnh này còn hiệu lực, kết quả câu truy vấn = 0
        Date timeNow = new Date();
        return this.certificateRepository.checkCertificateOfEmployeeValid(employeeId, provinceId, certificateId, timeNow) == 0;
    }

    @Override
    public boolean checkExistById(UUID id) {
        return this.certificateRepository.existsById(id);
    }

    @Override
    public boolean checkExistByIdAndName(UUID id, String name) {
        return this.certificateRepository.checkCertificateNameExist(id, name) > 0;
    }

    @Override
    public boolean checkEmployeeCertificateExistById(UUID id) {
        return this.employeeCertificateRepository.existsById(id);
    }

    @Override
    public void delete(UUID id) {

    }

    @Override
    public int countCertificateOfEmployeeValidById(UUID employeeId, UUID certificateId) {
        Date timeNow = new Date();
        return this.certificateRepository.countNumberCertificateOfEmployeeValid(employeeId, certificateId, timeNow);
    }

    private Certificate findCertificateEntityById(UUID id){
        return this.certificateRepository.findById(id).orElseThrow(() -> {
            String idNotFound = String.format(CERTIFICATE_ERRORS.CERTIFICATE_ID_NOT_FOUND, id);
            return new NotFoundException(idNotFound);
        });
    }
}

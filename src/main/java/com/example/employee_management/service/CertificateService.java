package com.example.employee_management.service;

import com.globits.da.dto.CertificateDto;
import com.globits.da.dto.EmployeeCertificateDto;

import java.util.List;
import java.util.UUID;

public interface CertificateService {
    List<CertificateDto> findAll();
    CertificateDto findOneById(UUID id);
    CertificateDto saveCertificate(CertificateDto saveCertificate);
    CertificateDto updateCertificate(CertificateDto updateCertificateDto);
    EmployeeCertificateDto giveCertificatesToEmployee(EmployeeCertificateDto employeeCertificateDto);
    boolean checkCertificateOfEmployeeValid(UUID employeeId, UUID provinceId, UUID certificateId);
    boolean checkExistById(UUID id);
    boolean checkExistByIdAndName(UUID id, String name);
    boolean checkEmployeeCertificateExistById(UUID id);
    void delete(UUID id);
    int countCertificateOfEmployeeValidById(UUID employeeId, UUID cerertificateId);
}

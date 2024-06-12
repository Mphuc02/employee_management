package com.example.employee_management.repository;


import com.globits.da.domain.Certificate;
import com.globits.da.dto.CertificateDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface CertificateRepository extends JpaRepository<Certificate, UUID> {
    @Query("Select new com.globits.da.dto.CertificateDto(c) from Certificate c")
    List<CertificateDto> findAllDtos();
    @Query("Select count(c.id) from EmployeeCertificate c " +
            "where " +
            "c.employee.id = :employeeId " +
            "and " +
            "c.province.id = :provinceId " +
            "and " +
            "c.certificate.id = :certificateId " +
            "and " +
            "c.expireDate > :timeNow")
    int checkCertificateOfEmployeeValid(UUID employeeId, UUID provinceId, UUID certificateId, Date timeNow);
    @Query("Select count(c.id) from EmployeeCertificate c " +
            "where " +
            "c.employee.id = :employeeId " +
            "and " +
            "c.certificate.id = :certificateId " +
            "and " +
            "c.expireDate > :timeNow")
    int countNumberCertificateOfEmployeeValid(UUID employeeId, UUID certificateId, Date timeNow);
    @Query("Select count(c.id) from Certificate c where c.id <> :id and c.name = :name")
    int checkCertificateNameExist(UUID id, String name);
}
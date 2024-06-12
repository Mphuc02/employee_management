package com.example.employee_management.repository;

import com.globits.da.domain.EmployeeCertificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeCertificateRepository extends JpaRepository<EmployeeCertificate, UUID> {
    boolean existsById(UUID id);
}
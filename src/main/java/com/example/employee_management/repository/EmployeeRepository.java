package com.example.employee_management.repository;

import com.globits.da.domain.Employee;
import com.globits.da.dto.EmployeeDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    @Query("select e from Employee e where e.id = :id")
    Employee test(UUID id);
    @Query("SELECT new com.globits.da.dto.EmployeeDto(e) FROM Employee e")
    List<EmployeeDto> getAllEmployee();
    boolean existsByCode(String code);
    boolean existsByEmail(String email);
}

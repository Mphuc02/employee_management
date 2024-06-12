package com.example.employee_management.service;

import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.search.SearchEmployeeDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public interface EmployeeService {
    List<EmployeeDto> findAll();
    SearchEmployeeDto findByContition(SearchEmployeeDto employeeDto);
    List<EmployeeDto> saveEmployeesFromExcel(MultipartFile file);
    EmployeeDto findOneById(UUID employeeId);
    EmployeeDto saveEmployee(EmployeeDto employeeDto);
    EmployeeDto updateEmployee(UUID id ,EmployeeDto updateEmployeeDto);
    boolean checkIdExisted(UUID id);
    boolean checkCodeExisted(String code);
    boolean checkEmailExisted(String email);
    void deleteById(UUID id);
}

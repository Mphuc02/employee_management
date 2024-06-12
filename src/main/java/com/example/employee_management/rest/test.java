package com.example.employee_management.rest;

import com.globits.da.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/test")
public class test {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private TestService testService;
}

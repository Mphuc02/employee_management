package com.example.employee_management.rest;

import com.globits.da.repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestService {
    @Autowired
    private ProvinceRepository provinceRepository;
}

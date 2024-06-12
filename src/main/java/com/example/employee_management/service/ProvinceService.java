package com.example.employee_management.service;

import com.globits.da.dto.ProvinceDto;
import com.globits.da.dto.search.SearchProvinceDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ProvinceService {
    List<ProvinceDto> findAll();
    ProvinceDto findById(UUID id);
    SearchProvinceDto findByCondition(SearchProvinceDto searchDto);
    ProvinceDto saveProvince(ProvinceDto provinceDto);
    ProvinceDto updateProvince(ProvinceDto provinceDto);
    boolean checkExistById(UUID id);
    boolean checkExsitByName(String name, UUID provinceId);
    boolean checkProvinceHasDistrictName(String name, UUID provinceId, UUID districtId);
    void delete(UUID id);
}
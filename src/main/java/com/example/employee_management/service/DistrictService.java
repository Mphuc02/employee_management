package com.example.employee_management.service;

import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.search.SearchDistrictDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface DistrictService {
    List<DistrictDto> findAll();
    SearchDistrictDto findAllByCondition(SearchDistrictDto SearchDistrictDto);
    DistrictDto findById(UUID id);
    DistrictDto saveDistrict(DistrictDto districtDto);
    DistrictDto updateDistrict(DistrictDto districtDto);
    boolean checkExistById(UUID id);
    boolean checkDistrictHasVillageName(String name, UUID districtId, UUID villageId);
    boolean checkProvinceHasDistrict(UUID provinceId, UUID districtId);
    void deleteDistrict(UUID id);
}

package com.example.employee_management.dto.search;

import com.globits.da.domain.Province;
import com.globits.da.dto.DistrictDto;

import java.util.List;

public class SearchDistrictDto extends SearchDto {
    private String name;
    private Province province;
    private List<DistrictDto> resultList;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public List<DistrictDto> getResultList() {
        return resultList;
    }

    public void setResultList(List<DistrictDto> resultList) {
        this.resultList = resultList;
    }
}

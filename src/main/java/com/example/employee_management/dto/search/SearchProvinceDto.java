package com.example.employee_management.dto.search;

import com.globits.da.dto.ProvinceDto;

import java.util.List;

public class SearchProvinceDto extends SearchDto {
    private String name;
    private List<ProvinceDto> resultList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProvinceDto> getResultList() {
        return resultList;
    }

    public void setResultList(List<ProvinceDto> resultList) {
        this.resultList = resultList;
    }
}

package com.example.employee_management.dto.search;

import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.VillageDto;

import java.util.List;

public class SearchVillageDto extends SearchDto{
    private String name;
    private Long population;
    private DistrictDto district;
    private List<VillageDto> resultList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPopulation() {
        return population;
    }

    public void setPopulation(Long population) {
        this.population = population;
    }

    public List<VillageDto> getResultList() {
        return resultList;
    }

    public void setResultList(List<VillageDto> resultList) {
        this.resultList = resultList;
    }

    public DistrictDto getDistrict() {
        return district;
    }

    public void setDistrict(DistrictDto district) {
        this.district = district;
    }
}

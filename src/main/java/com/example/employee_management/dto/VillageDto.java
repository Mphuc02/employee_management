package com.example.employee_management.dto;

import com.example.employee_management.entity.Village;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.ObjectUtils;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VillageDto extends BaseObjectDTO {
    private String name;
    private Long population;
    private DistrictDto district;

    private String[] messages;
    public VillageDto(){

    }
    public VillageDto(Village village){
        if(!ObjectUtils.isEmpty(village)){
            this.setId(village.getId());
            this.name = village.getName();
            this.population = village.getPopulation();
        }
    }

    public DistrictDto getDistrict() {
        if(ObjectUtils.isEmpty(district))
            district = new DistrictDto();
        return district;
    }
}

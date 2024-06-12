package com.example.employee_management.dto;

import com.example.employee_management.entity.District;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.ObjectUtils;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DistrictDto extends BaseObjectDTO {
    private String name;
    private ProvinceDto province;
    private String[] messages;
    private List<VillageDto> villages;
    public DistrictDto() {

    }

    public DistrictDto(District district){
        if(!ObjectUtils.isEmpty(district)){
            this.setId(district.getId());
            this.name = district.getName();
            if(!ObjectUtils.isEmpty(district.getVillages())){
                this.villages = district.getVillages().stream().map(VillageDto::new).collect(Collectors.toList());
            }
        }
    }

    public ProvinceDto getProvince() {
        if(ObjectUtils.isEmpty(this.province))
            province = new ProvinceDto();
        return province;
    }
}
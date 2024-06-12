package com.example.employee_management.dto;

import com.example.employee_management.entity.Province;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.ObjectUtils;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProvinceDto extends BaseObjectDTO {
    private String name;
    private List<DistrictDto> districts;

    private String[] messages;

    public ProvinceDto() {
    }

    public ProvinceDto(Province province){
        if(!ObjectUtils.isEmpty(province)){
            this.setId(province.getId());
            this.name = province.getName();
            if(!ObjectUtils.isEmpty(province.getDistricts())){
                this.districts = province.getDistricts().stream().map(DistrictDto::new).collect(Collectors.toList());
            }
        }
    }
}

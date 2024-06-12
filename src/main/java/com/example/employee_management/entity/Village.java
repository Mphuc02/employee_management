package com.example.employee_management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.ObjectUtils;

@Entity
@Table(name = "tbl_village")
@DynamicUpdate
@Getter
@Setter
public class Village extends BaseObject{
    @Column(length = 50)
    private String name;
    @Column
    private Long population;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id", nullable = false)
    private District district;

    public Village(){

    }

    public District getDistrict() {
        if(ObjectUtils.isEmpty(district))
            district = new District();
        return district;
    }
}

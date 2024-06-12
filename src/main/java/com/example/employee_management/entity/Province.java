package com.example.employee_management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.ObjectUtils;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_province")
@DynamicUpdate
@Getter
@Setter
public class Province extends BaseObject{
    @Column(length = 50)
    private String name;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "province")
    private List<District> districts;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "province", cascade = CascadeType.ALL)
    private List<Employee> employees;

    public Province(){

    }

    public List<District> getDistricts() {
        if(ObjectUtils.isEmpty(districts))
            districts = new ArrayList<>();
        return districts;
    }
}

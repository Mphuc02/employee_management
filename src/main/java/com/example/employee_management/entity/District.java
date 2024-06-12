package com.example.employee_management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.ObjectUtils;

import java.util.List;
@DynamicUpdate
@Entity
@Table(name = "tbl_district")
@Getter
@Setter
public class District extends BaseObject {
    @Column(length = 50)
    private String name;
    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id", nullable = false)
    private Province province;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "district")
    private List<Village> villages;
    public District(){
    }

    public Province getProvince() {
        if(ObjectUtils.isEmpty(province))
            province = new Province();
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

}

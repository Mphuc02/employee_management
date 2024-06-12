package com.example.employee_management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.ObjectUtils;
import java.util.List;

@DynamicUpdate
@Entity
@Table(name = "tbl_employee")
@Getter
@Setter
public class Employee extends BaseObject {
    @Column(name = "code", length = 20)
    private String code;
    @Column(name = "name", length = 50)
    private String name;
    @Column(name = "email", length = 50)
    private String email;
    @Column(name = "phone", length = 11)
    private String phone;
    @Column(name = "age")
    private Integer age;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id")
    private Province province;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id")
    private District district;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "village_id")
    private Village village;
    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    private List<EmployeeCertificate> certificates;

    public Province getProvince() {
        if(ObjectUtils.isEmpty(province) )
            province = new Province();
        return province;
    }
    public void setProvince(Province province) {
        this.province = province;
    }
    public District getDistrict() {
        if(ObjectUtils.isEmpty(district))
            district = new District();
        return district;
    }
    public void setDistrict(District district) {
        this.district = district;
    }
    public Village getVillage() {
        if(ObjectUtils.isEmpty(village))
            village = new Village();
        return village;
    }
}
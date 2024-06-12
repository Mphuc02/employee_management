package com.example.employee_management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.ObjectUtils;
import java.sql.Date;

@DynamicUpdate
@Entity
@Table(name = "tbl_employee_certificate")
@Getter
@Setter
public class EmployeeCertificate extends BaseObject {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certificate_id")
    private Certificate certificate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id")
    private Province province;
    @Column(name = "valid_date")
    private Date validDate;
    @Column(name = "expire_date")
    private Date expireDate;

    public Certificate getCertificate() {
        if(ObjectUtils.isEmpty(certificate))
            certificate = new Certificate();
        return certificate;
    }
    public void setCertificate(Certificate certificate) {
        if(ObjectUtils.isEmpty(certificate))
            certificate = new Certificate();
        this.certificate = certificate;
    }

    public Province getProvince() {
        if(ObjectUtils.isEmpty(province))
            province = new Province();
        return province;
    }

    public Employee getEmployee() {
        if(ObjectUtils.isEmpty(employee))
            employee = new Employee();
        return employee;
    }
}
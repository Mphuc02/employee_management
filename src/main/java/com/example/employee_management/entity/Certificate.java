package com.example.employee_management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import java.util.List;

@DynamicUpdate
@Entity
@Table(name = "tbl_Certificate")
@Getter
@Setter
public class Certificate extends BaseObject {
    @OneToMany(mappedBy = "certificate")
    private List<EmployeeCertificate> employees;
    @Column(length = 50)
    private String name;

    public Certificate() {
    }
}

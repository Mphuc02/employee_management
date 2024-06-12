package com.example.employee_management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.VarcharJdbcType;

import java.util.UUID;

@Entity
@Getter
@Setter
public class BaseObject {
    @Id
    @JdbcType(VarcharJdbcType.class)
    private UUID id;
}
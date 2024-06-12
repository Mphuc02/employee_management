package com.example.employee_management.dto.search;

import com.globits.da.dto.EmployeeDto;

import java.util.List;

public class SearchEmployeeDto extends SearchDto{
    private String code;

    private String name;

    private String email;

    private String phone;

    private Integer age;
    List<EmployeeDto> resultList;

    public SearchEmployeeDto(){
        super();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<EmployeeDto> getResultList() {
        return resultList;
    }

    public void setResultList(List<EmployeeDto> resultList) {
        this.resultList = resultList;
    }
}

package com.example.employee_management.utils;

import org.springframework.stereotype.Component;

@Component
public class ReFormatNameUtils {
    public String reFormatName(String name){
        //Chuẩn hóa lại tên của tỉnh
        String[] nameTokens = name.split("\\s+");
        name = "";
        for(String nameToken: nameTokens){
            name += nameToken.substring(0,1).toUpperCase() + nameToken.substring(1).toLowerCase() + " ";
        }

        //Xóa khoảng trắng ở cuối chuỗi
        return name.trim();
    }

}

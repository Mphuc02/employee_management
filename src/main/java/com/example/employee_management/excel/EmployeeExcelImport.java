package com.example.employee_management.excel;

import com.globits.da.constant.Constant.*;
import com.globits.da.constant.ErrorMessage.*;
import com.globits.da.domain.Employee;
import com.globits.da.exception.FileNotFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EmployeeExcelImport {
        public Employee getEmployeeFromRow(Row row){
        if(row.getPhysicalNumberOfCells() != EMPLOYEE.NUMBER_FIELD_CELL_IN_A_EXCEL_ROW){
            String notEnoughCellInRow = String.format(FILE_ERRORS.NOT_ENOUGH_CELL_IN_ROW, EMPLOYEE.NUMBER_FIELD_CELL_IN_A_EXCEL_ROW);

            throw new FileNotFormatException(notEnoughCellInRow);
        }

        Employee employee = new Employee();

        //Lấy ra các thuộc tính từng cột trên dòng này
        employee.setName(row.getCell(0).toString());
        employee.setCode(row.getCell(1).toString());
        employee.setEmail(row.getCell(2).toString());
        employee.setPhone(row.getCell(3).toString());
        try {
            employee.setAge( (int) Double.parseDouble(row.getCell(4).toString()));
        }catch (NumberFormatException ex){
            throw new FileNotFormatException(EMPLOYEE_ERRORS.AGE_NOT_FORMAT_EXCEL);
        }

        try {
            employee.getProvince().setId( UUID.fromString(row.getCell(5).toString()));
        }catch(IllegalArgumentException ex){
            throw new FileNotFormatException(EMPLOYEE_ERRORS.PROVINCE_ID_EXCEL_NOT_FORMAT);
        }

        try{
            employee.getDistrict().setId( UUID.fromString(row.getCell(6).toString()));
        }catch (IllegalArgumentException ex){
            throw new FileNotFormatException(EMPLOYEE_ERRORS.DISTRICT_ID_EXCEL_NOT_FORMAT);
        }

        try{
            employee.getVillage().setId( UUID.fromString(row.getCell(7).toString()));
        }catch (IllegalArgumentException ex){
            throw new FileNotFormatException(EMPLOYEE_ERRORS.VILLAGE_ID_EXCEL_NOT_FORMAT);
        }

        return employee;
    }
}

package com.example.employee_management.rest;

import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.search.SearchEmployeeDto;
import com.globits.da.excel.EmployeeExcelExport;
import com.globits.da.exception.ObjectFieldNotValidException;
import com.globits.da.service.EmployeeService;
import com.globits.da.validation.condition.OnCreate;
import com.globits.da.validation.condition.OnDefault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("employee")
public class RestEmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public RestEmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping()
    public List<EmployeeDto> findAllEmployee() {
        return this.employeeService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findOneById(@PathVariable UUID id) {
        EmployeeDto employee = this.employeeService.findOneById(id);
        return ResponseEntity.ok(employee);
    }

    @PostMapping(value = "/find")
    public SearchEmployeeDto findByCondition(@RequestBody SearchEmployeeDto employeeDto) {
        return this.employeeService.findByContition(employeeDto);
    }

    @PostMapping(value = "/excel")
    public void exportEmployyeeToExcel(@RequestBody(required = false) SearchEmployeeDto employeeDto, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=employess.xlsx";
        response.setHeader(headerKey, headerValue);

        List<EmployeeDto> listEmployees = this.employeeService.findByContition(employeeDto).getResultList();
        EmployeeExcelExport exportExcel = new EmployeeExcelExport(listEmployees);
        exportExcel.export(response);
    }

    @PostMapping()
     //Validated Employee theo quy chuẩn khi tạo mới, yêu cầu tất cả các field khác null
    public ResponseEntity<Object> saveEmpoyee(@Validated({OnCreate.class, OnDefault.class}) @Valid @RequestBody EmployeeDto employeeDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            throw new ObjectFieldNotValidException(bindingResult.getFieldErrors());
        }
        EmployeeDto saveEmployee = this.employeeService.saveEmployee(employeeDto);
        return ResponseEntity.ok(saveEmployee);
    }

    @PostMapping("/import-employees-excel")
    public ResponseEntity<Object> importEmployeesByExcel(@RequestParam MultipartFile file) {
        return ResponseEntity.ok( this.employeeService.saveEmployeesFromExcel(file) );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateEmployee(@PathVariable UUID id , @Validated(OnDefault.class) @RequestBody EmployeeDto employeeDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            throw new ObjectFieldNotValidException(bindingResult.getFieldErrors());
        }
        EmployeeDto updateEmployee = this.employeeService.updateEmployee(id,employeeDto);
        return ResponseEntity.ok(updateEmployee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteEmployee(@PathVariable(name = "id") UUID employeeiD) {
        this.employeeService.deleteById(employeeiD);
        return ResponseEntity.ok("xoá nhân viên thành công!");
    }
}

package com.example.employee_management.service.impl;

import com.globits.da.constant.Constant.*;
import com.globits.da.constant.ErrorMessage.*;
import com.globits.da.domain.Employee;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.search.SearchEmployeeDto;
import com.globits.da.excel.EmployeeExcelImport;
import com.globits.da.exception.FileNotFormatException;
import com.globits.da.exception.NotFoundException;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.service.EmployeeService;
import com.globits.da.utils.PagingUtil;
import com.globits.da.validation.EmployeeValidation;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Service(value = BEAN_SERVICE.EMPLOYEE_SERVICE)
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EntityManager entityManager;
    private final EmployeeValidation employeeValidation;
    private final EmployeeExcelImport employeeExcelImport;
    private final PagingUtil pagingUtil;
    private final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository repository,
                               EntityManager manager,
                               EmployeeValidation employeeValidation,
                               EmployeeExcelImport employeeExcelImport,
                               PagingUtil pagingUtil) {
        this.employeeRepository = repository;
        this.entityManager = manager;
        this.employeeValidation = employeeValidation;
        this.employeeExcelImport = employeeExcelImport;
        this.pagingUtil = pagingUtil;
    }

    @Override
    public List<EmployeeDto> findAll() {
        return employeeRepository.getAllEmployee();
    }

    @Override
    public SearchEmployeeDto findByContition(SearchEmployeeDto employeeDto) {
        StringBuilder selectSql = new StringBuilder("SELECT new com.globits.da.dto.EmployeeDto(e) FROM Employee e");
        StringBuilder countSql = new StringBuilder("Select count(e.id) from Employee e");

        this.addAndClause(employeeDto, selectSql, countSql);

        Query selectQuery = entityManager.createQuery(selectSql.toString());
        Query countQuery = entityManager.createQuery(countSql.toString());

        this.setParameters(employeeDto, selectQuery, countQuery);
        this.pagingUtil.setPageSearchDto(employeeDto, selectQuery);

        employeeDto.setTotalPage( (int) Math.ceil( countQuery.getFirstResult() / employeeDto.getPageSize()));
        employeeDto.setResultList(selectQuery.getResultList());

        return employeeDto;
    }

    @Override
    public List<EmployeeDto> saveEmployeesFromExcel(MultipartFile file) {
        List<EmployeeDto> result = new ArrayList<>();
        List<Employee> waitToSaveList = new ArrayList<>();
        try(Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.rowIterator();
            while (rows.hasNext()){
                Row row = rows.next();
                try{
                    Employee employee = this.employeeExcelImport.getEmployeeFromRow(row);
                    EmployeeDto checkEmployee = new EmployeeDto(employee);
                    this.employeeValidation.checkValid(checkEmployee, VALIDATION.CREATE, null);

                    result.add(checkEmployee);
                    waitToSaveList.add(employee);
                }catch (RuntimeException ex){
                    logger.error(ex.getMessage(), ex);

                    String[] errors = new String[1];
                    errors[0] = String.format(EMPLOYEE_ERRORS.ERROR_AT_EXCEL_ROW + ex.getMessage(), result.size() + 1);
                    EmployeeDto errorEmployee = new EmployeeDto();
                    errorEmployee.setMessage(errors);

                    result.add(errorEmployee);
                }
            }

            this.employeeRepository.saveAll(waitToSaveList);
        }catch (IOException | InvalidFormatException ex){
            throw new FileNotFormatException(FILE_ERRORS.FILE_NOT_IN_EXCEL_FORMAT);
        }
        return result;
    }

    @Override
    public EmployeeDto findOneById(UUID employeeId) {
        return new EmployeeDto(this.findEmployeeEntityById(employeeId));
    }

    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {
        Employee employee = new Employee();
        //Kiểm tra các dữ liệu
        this.employeeValidation.checkValid(employeeDto, VALIDATION.CREATE, employee);
        employee = this.employeeRepository.save(employee);
        return new EmployeeDto(employee);
    }

    @Override
    public EmployeeDto updateEmployee(UUID id,EmployeeDto updateEmployeeDto) {
        updateEmployeeDto.setId(id);
        Employee findToUpdate = this.findEmployeeEntityById(updateEmployeeDto.getId());
        //Kiểm tra các thuộc tính của đối tượng
        this.employeeValidation.checkValid(updateEmployeeDto, VALIDATION.UPDATE, findToUpdate);
        //thực hiện gọi hàm lưu các thay đổi nếu các dữ liệu là hợp lệ
        this.employeeRepository.save(findToUpdate);
        //trả về toàn bộ thông tin đã được cập nhật của Employee
        return new EmployeeDto(findToUpdate);
    }

    @Override
    public boolean checkIdExisted(UUID id) {
        return this.employeeRepository.existsById(id);
    }
    @Override
    public boolean checkCodeExisted(String code) {
        return this.employeeRepository.existsByCode(code);
    }
    @Override
    public boolean checkEmailExisted(String email) {
        return this.employeeRepository.existsByEmail(email);
    }
    @Override
    public void deleteById(UUID id){
        //Nếu không tìm thấy employee
        if(!this.employeeRepository.existsById(id)){
            String idNotExisted = String.format(EMPLOYEE_ERRORS.ID_NOT_FOUND, id);
            throw new NotFoundException(idNotExisted);
        }
        employeeRepository.deleteById(id);
    }

    private void addAndClause(SearchEmployeeDto dto,StringBuilder... sqls) {
        StringBuilder andClause = new StringBuilder(" where (1 = 1)");

        if(!ObjectUtils.isEmpty(dto.getId())) {
            andClause.append(" AND id = :id");
        }
        if(!ObjectUtils.isEmpty(dto.getAge())) {
            andClause.append(" AND age = :age");
        }
        if(!ObjectUtils.isEmpty(dto.getCode())) {
            andClause.append("AND lower(code) LIKE lower(:code)");
        }
        if(!ObjectUtils.isEmpty(dto.getEmail())) {
            andClause.append(" AND lower(email) like lower(:email)");
        }
        if(!ObjectUtils.isEmpty(dto.getName())) {
            andClause.append(" AND lower(name) like lower(:name)");
        }
        if(!ObjectUtils.isEmpty(dto.getPhone())) {
            andClause.append(" AND phone = :phone");
        }

        for(StringBuilder sql: sqls){
            sql.append(andClause);
        }
    }

    private void setParameters(SearchEmployeeDto dto, Query... queries){
        for(Query query: queries){
            if(!ObjectUtils.isEmpty(dto.getId())) {
                query.setParameter("id", dto.getId());
            }
            if(!ObjectUtils.isEmpty(dto.getAge())) {
                query.setParameter("age", dto.getAge());
            }
            if(!ObjectUtils.isEmpty(dto.getCode())) {
                query.setParameter("code","%" + dto.getCode() + "%");
            }
            if(!ObjectUtils.isEmpty(dto.getEmail())) {
                query.setParameter("email", "%" + dto.getEmail() + "%");
            }
            if(!ObjectUtils.isEmpty(dto.getName())) {
                query.setParameter("name", "%" + dto.getName() + "%");
            }
            if(!ObjectUtils.isEmpty(dto.getPhone())) {
                query.setParameter("phone", dto.getPhone());
            }
        }
    }

    private Employee findEmployeeEntityById(UUID id){
        return this.employeeRepository.findById(id).orElseThrow(() -> {
            String idNotFound = String.format(EMPLOYEE_ERRORS.ID_NOT_FOUND, id);
            return new NotFoundException(idNotFound);
        });
    }
}

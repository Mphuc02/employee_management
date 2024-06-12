package com.example.employee_management.exception;

import org.springframework.validation.FieldError;

import java.util.List;

public class ObjectFieldNotValidException extends RuntimeException{
    private List<FieldError> fieldErrors;
    public ObjectFieldNotValidException(){
    }

    public ObjectFieldNotValidException(List<FieldError> fieldErrors){
        this.fieldErrors = fieldErrors;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}

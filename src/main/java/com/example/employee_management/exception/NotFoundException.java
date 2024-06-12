package com.example.employee_management.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String error){
        super(error);
    }
}

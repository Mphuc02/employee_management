package com.example.employee_management.exception;

public class DuplicateException extends RuntimeException{
    public DuplicateException(String error){
        super(error);
    }
}

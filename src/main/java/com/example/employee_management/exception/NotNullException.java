package com.example.employee_management.exception;

public class NotNullException extends RuntimeException{
    public NotNullException(){
        super();
    }

    public NotNullException(String error){
        super(error);
    }
}

package com.example.employee_management.exception;

public class FileNotFormatException extends RuntimeException{
    private String message;

    public FileNotFormatException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

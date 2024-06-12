package com.example.employee_management.utils;

import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class ConvertErrorMessages {
    public Map<String, String> getErrorMessages(List<FieldError> fieldErrors) {
        Map<String, String> errors = new LinkedHashMap<>();
        fieldErrors.forEach(field -> errors.put(field.getField(), field.getDefaultMessage()));
        return errors;
    }
}

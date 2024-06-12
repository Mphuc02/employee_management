package com.example.employee_management.handlerException;

import com.globits.da.exception.*;
import com.globits.da.utils.ConvertErrorMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class HandlerException {
    private final ConvertErrorMessages convertErrorMessages;
    private final Logger logger = LoggerFactory.getLogger(HandlerException.class);

    @Autowired
    public HandlerException(ConvertErrorMessages convertErrorMessages){
        this.convertErrorMessages = convertErrorMessages;
    }

    @ExceptionHandler(NotNullException.class)
    public ResponseEntity<Object> notNullHander(NotNullException ex){
        logger.error("null pointer", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> notFoundHandler(NotFoundException ex){
        logger.error("not found", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<Object> duplicateHandler(DuplicateException  ex){
        logger.error("duplicate", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ObjectFieldNotValidException.class)
    public ResponseEntity<Object> objectFieldHandler(ObjectFieldNotValidException ex){
        logger.error("object not valid", ex);
        Map<String, String> errors = this.convertErrorMessages.getErrorMessages(ex.getFieldErrors());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileNotFormatException.class)
    public ResponseEntity<Object> fileNotFormatHandler(FileNotFormatException ex){
        logger.error("file is not format", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}

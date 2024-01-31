package com.example.security.controller;

import com.example.security.utils.LogMessage;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Global exception handler advice for handling various exceptions and providing appropriate responses.
 */
@RestControllerAdvice
@ControllerAdvice
public class ExceptionHandlerAdvice {

    /**
     * Handles BadRequestException and returns a ResponseEntity with a log message and HttpStatus.BAD_REQUEST.
     */
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex) {
        return new ResponseEntity<>(LogMessage.logInfo(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleBadRequestException(IllegalArgumentException ex) {
        return new ResponseEntity<>(LogMessage.logInfo(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles DataIntegrityViolationException and returns a ResponseEntity with a log message and HttpStatus.BAD_REQUEST.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleBadRequestException(DataIntegrityViolationException ex) {
        return new ResponseEntity<>(LogMessage.logInfo(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles MethodArgumentNotValidException and returns a ResponseEntity with a map of field errors and HttpStatus.BAD_REQUEST.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleBindingException(MethodArgumentNotValidException ex){
        BindingResult bindingResult = ex.getBindingResult();
        Map<String, String> errors = new HashMap<>();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }


    /**
     *  Handles generic RuntimeException and returns a 500 Internal Server Error response.
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleGenericException(RuntimeException ex) {
        return new ResponseEntity<>(LogMessage.logInfo(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

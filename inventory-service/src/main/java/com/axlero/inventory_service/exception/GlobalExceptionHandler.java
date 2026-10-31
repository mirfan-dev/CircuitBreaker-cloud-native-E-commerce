package com.axlero.inventory_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex){

        Map<String,Object> error =  new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err-> error.put(err.getField(),err.getDefaultMessage()));
        Map<String,Object> map =  Map.of("Error","Validation failed","message",ex.getMessage());
        return ResponseEntity.badRequest().body(map);
    }


    @ExceptionHandler(InventoryNotFoundException.class)
    public ResponseEntity<Map<String, Object>> hadleNotFound(InventoryNotFoundException ex){

        Map<String,Object> map =  Map.of("Error","Not Found","message",ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
    }

    @ExceptionHandler(InventoryAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> hadleResourceAlreadyExist(InventoryAlreadyExistsException ex){

        Map<String,Object> map =  Map.of("Error","Conflict","message",ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(map);
    }
}

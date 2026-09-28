package com.example.attendance.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private Map<String,Object> body(String message) {
        return Map.of("timestamp", LocalDateTime.now(), "message", message);
    }
    @ExceptionHandler(ApiException.class)
    ResponseEntity<?> api(ApiException e) { return ResponseEntity.status(e.status()).body(body(e.getMessage())); }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<?> validation(MethodArgumentNotValidException e) {
        Map<String,String> errors = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(x -> errors.put(x.getField(), x.getDefaultMessage()));
        return ResponseEntity.badRequest().body(Map.of("message","Validation failed","errors",errors));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<?> duplicate() {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body("Duplicate value or record violates a database constraint"));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<?> other(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body("Unexpected server error"));
    }
}

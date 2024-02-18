package com.momo.taskManagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handlingAccessDeniedException(AccessDeniedException e){

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("You are not allowed to access this content");
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handlingUserNotFoundException(UserNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<String> handlingTaskNotFoundException(TaskNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(InvalidCountryOrLanguageCodeException.class)
    public ResponseEntity<String > handlingInvalidCountryOrLanguageCodeException(InvalidCountryOrLanguageCodeException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

}

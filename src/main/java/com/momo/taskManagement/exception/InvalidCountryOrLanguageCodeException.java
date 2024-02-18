package com.momo.taskManagement.exception;

public class InvalidCountryOrLanguageCodeException extends RuntimeException{
    public InvalidCountryOrLanguageCodeException(String message){
         super(message);
    }
}

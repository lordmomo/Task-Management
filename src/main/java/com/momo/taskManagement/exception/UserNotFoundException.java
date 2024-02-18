package com.momo.taskManagement.exception;



public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message){
        super(message);
    }


}

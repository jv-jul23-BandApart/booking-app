package com.bookingapp.exception;

public class ChangingRoleException extends RuntimeException{
    public ChangingRoleException(String message) {
        super(message);
    }
    
    public ChangingRoleException(String message, Throwable cause) {
        super(message, cause);
    }
}

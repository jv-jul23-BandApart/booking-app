package com.bookingapp.exception;

public class BookingException extends RuntimeException {
    public BookingException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookingException(String message) {
        super(message);
    }
}
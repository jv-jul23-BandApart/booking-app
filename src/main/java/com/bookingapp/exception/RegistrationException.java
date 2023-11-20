package com.bookingapp.exception;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RegistrationException extends RuntimeException {
    public RegistrationException(@NotBlank @Email String message) {
        super(message);
    }
}

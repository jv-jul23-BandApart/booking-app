package com.bookingapp.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UserRegistrationRequestDto(
        @NotBlank
        @Email
        String email,
        @Length(min = 4, max = 50)
        @NotBlank
        String password,
        @Length(min = 4, max = 50)
        @NotBlank
        String repeatPassword,
        @Length(min = 4, max = 50)
        @NotBlank
        String firstName,
        @Length(min = 4, max = 50)
        @NotBlank
        String lastName
) {
}

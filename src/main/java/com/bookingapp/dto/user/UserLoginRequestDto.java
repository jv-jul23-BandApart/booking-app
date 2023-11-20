package com.bookingapp.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UserLoginRequestDto(
        @NotBlank
        @Email(message = "must match pattern 'username@domain.com'")
        String email,
        @Length(min = 4, max = 50)
        @NotBlank
        String password
) {
}

package com.bookingapp.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequestDto(
        @Email
        String email,
        @NotBlank(message = "Your firstname field should contain 1 symbol at least")
        @Size(max = 60, message = "Your firstname should be less than 60 symbols")
        String firstName,
        @NotBlank(message = "Your firstname field should contain 1 symbol at least")
        @Size(max = 60, message = "Your first name should be less than 60 symbols")
        String lastName
) {
}

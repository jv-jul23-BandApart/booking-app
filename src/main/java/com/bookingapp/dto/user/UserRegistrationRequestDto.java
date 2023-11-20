package com.bookingapp.dto.user;

import com.bookingapp.validation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

@FieldMatch(fields = {"password", "repeatPassword"},message = "The password fields must match")
public record UserRegistrationRequestDto(
        @NotBlank
        @Email
        String email,
        @Length(min = 4, max = 50, message = "length should be from 8 to 50")
        @NotNull
        @Pattern(
                regexp = "^[a-zA-Z0-9]{6,32}$",
                message = "Password must contain at least one digit and letter"
        )
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

package com.bookingapp.validation;

import com.bookingapp.dto.user.UserRegistrationRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch,
        UserRegistrationRequestDto> {
    @Override
    public boolean isValid(UserRegistrationRequestDto value, ConstraintValidatorContext context) {
        String password = value.password();
        String repeatPassword = value.repeatPassword();
        return password != null && password.equals(repeatPassword);
    }
}

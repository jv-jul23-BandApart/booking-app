package com.bookingapp.validation;

import com.bookingapp.dto.booking.BookingRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class CheckInDateBeforeValidator implements ConstraintValidator<CheckInDateBefore,
        BookingRequestDto> {
    @Override
    public boolean isValid(BookingRequestDto value, ConstraintValidatorContext context) {
        LocalDate checkIn = value.checkInDate();
        LocalDate checkOut = value.checkOutDate();
        return checkIn != null && checkIn.isBefore(checkOut);
    }
}

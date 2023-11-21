package com.bookingapp.dto.booking;

import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record BookingUpdateDto(
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate checkInDate,
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate checkOutDate
) {
}

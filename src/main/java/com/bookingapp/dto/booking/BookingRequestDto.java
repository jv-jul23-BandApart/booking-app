package com.bookingapp.dto.booking;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record BookingRequestDto(
        @FutureOrPresent
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate checkInDate,
        @Future
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate checkOutDate,
        @Positive
        Long accommodationId) {
}

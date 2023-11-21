package com.bookingapp.dto.booking;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

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

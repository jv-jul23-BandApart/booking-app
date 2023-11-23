package com.bookingapp.dto.booking;

import com.bookingapp.validation.CheckInDateBefore;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

@CheckInDateBefore(fields = {"checkInDate", "checkOutDate"})
public record BookingRequestDto(
        @FutureOrPresent
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate checkInDate,
        @Future
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate checkOutDate,
        @Positive
        @NotNull
        Long accommodationId) {
}

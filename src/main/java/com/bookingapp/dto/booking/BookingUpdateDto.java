package com.bookingapp.dto.booking;

import java.time.LocalDate;

import com.bookingapp.validation.CheckInDateBefore;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import org.springframework.format.annotation.DateTimeFormat;

@CheckInDateBefore(fields = {"checkInDate", "checkOutDate"})
public record BookingUpdateDto(
        @FutureOrPresent
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate checkInDate,
        @Future
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate checkOutDate
) {
}

package com.bookingapp.dto.booking;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record BookingUpdateDto(
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate checkInDate,
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate checkOutDate
) {
}

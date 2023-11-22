package com.bookingapp.dto.booking;

public record BookingDto(
        Long id,
        String checkInDate,
        String checkOutDate,
        Long accommodationId,
        Long userId,
        String status
) {
}

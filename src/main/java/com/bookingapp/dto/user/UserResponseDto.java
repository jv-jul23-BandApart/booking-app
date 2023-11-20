package com.bookingapp.dto.user;

public record UserResponseDto(
        Long id,
        String email,
        String lastName,
        String firstName
) {
}

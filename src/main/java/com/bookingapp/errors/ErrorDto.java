package com.bookingapp.errors;

import java.time.LocalDateTime;

public record ErrorDto(
        LocalDateTime timestamp,
        Object errorPayload) {
}

package com.bookingapp.dto.accommodation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;

public record AccommodationRequestDto(
        @NotBlank(message = "Type should not be blank or empty")
        String type,
        @NotBlank(message = "Location should not be blank or empty")
        String location,
        @NotBlank(message = "Size should not be blank or empty")
        String size,
        List<String> amenities,
        @Positive(message = "Only positive value allowed")
        @NotNull(message = "Daily rate must not be null")
        BigDecimal dailyRate,
        @Positive(message = "Only positive value allowed")
        @NotNull(message = "Availability must not be null")
        Integer availability) {
}

package com.bookingapp.dto.accommodation;

import java.math.BigDecimal;
import java.util.List;

public record AccommodationDto(
        Long id,
        String type,
        String location,
        String size,
        List<String> amenities,
        BigDecimal dailyRate,
        int availability) {
}

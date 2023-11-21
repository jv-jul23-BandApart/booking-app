package com.bookingapp.dto.user;

import java.util.Set;

public record UserWithRolesResponseDto(
        String email,
        String firstName,
        String lastName,
        Set<Long> roleIds
) {
}

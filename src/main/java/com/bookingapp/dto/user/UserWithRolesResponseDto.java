package com.bookingapp.dto.user;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserWithRolesResponseDto {
    private String email;
    private String firstName;
    private String lastName;
    private Set<Long> roleIds;
}

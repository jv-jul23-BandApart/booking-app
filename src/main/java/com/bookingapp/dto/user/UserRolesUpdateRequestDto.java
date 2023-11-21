package com.bookingapp.dto.user;

import jakarta.validation.constraints.NotEmpty;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRolesUpdateRequestDto {
    @NotEmpty(message = "You should enter one role id at least")
    private Set<Long> roleIds;

}

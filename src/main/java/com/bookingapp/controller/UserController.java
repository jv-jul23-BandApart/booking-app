package com.bookingapp.controller;

import com.bookingapp.dto.user.UserResponseDto;
import com.bookingapp.dto.user.UserRolesUpdateRequestDto;
import com.bookingapp.dto.user.UserUpdateRequestDto;
import com.bookingapp.dto.user.UserWithRolesResponseDto;
import com.bookingapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    
    @Operation(summary = "Get an information about current logged-in user",
            description = """
                    Get a profile information(id, email, firstname, lastname)
                     about current logged-in user from DB
                    """)
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/me")
    public UserResponseDto getProfile() {
        return userService.getCurrentUserProfileInfo();
    }
    
    @Operation(summary = "Update an information in current logged-in user profile",
            description = """
                    Update an  information(email, firstname, lastname) in current
                    logged-in user profile in DB""")
    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/me", method = {RequestMethod.PATCH, RequestMethod.PUT})
    public UserResponseDto updateProfile(@RequestBody @Valid UserUpdateRequestDto requestDto) {
        return userService.updateCurrentUserProfileInfo(requestDto);
    }
    
    @Operation(summary = "Update roles by id",
            description = "Update user roles by user id in DB")
    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping("/{id}/role")
    public UserWithRolesResponseDto updateUserRoles(
            @PathVariable @Positive Long id,
            @RequestBody @Valid UserRolesUpdateRequestDto requestDto) {
        return userService.updateUserRoles(id, requestDto);
    }
}

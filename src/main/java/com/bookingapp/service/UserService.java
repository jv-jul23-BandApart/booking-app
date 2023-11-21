package com.bookingapp.service;

import com.bookingapp.dto.user.UserRegistrationRequestDto;
import com.bookingapp.dto.user.UserResponseDto;
import com.bookingapp.dto.user.UserRolesUpdateRequestDto;
import com.bookingapp.dto.user.UserUpdateRequestDto;
import com.bookingapp.dto.user.UserWithRolesResponseDto;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto);
    
    UserResponseDto getCurrentUserProfileInfo();
    
    UserResponseDto updateCurrentUserProfileInfo(UserUpdateRequestDto requestDto);
    
    UserWithRolesResponseDto updateUserRoles(Long id, UserRolesUpdateRequestDto requestDto);
}

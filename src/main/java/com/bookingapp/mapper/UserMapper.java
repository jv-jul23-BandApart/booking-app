package com.bookingapp.mapper;

import com.bookingapp.dto.user.UserRegistrationRequestDto;
import com.bookingapp.dto.user.UserResponseDto;
import com.bookingapp.dto.user.UserUpdateRequestDto;
import com.bookingapp.dto.user.UserWithRolesResponseDto;
import com.bookingapp.model.Role;
import com.bookingapp.model.User;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper {
    UserResponseDto toUserResponse(User user);

    User toUser(UserRegistrationRequestDto registrationRequestDto);
    
    UserWithRolesResponseDto toDtoWithRoles(User user);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mapUpdateRequestToUser(UserUpdateRequestDto requestDto, @MappingTarget User user);
    
    @AfterMapping
    default void setRoleIds(@MappingTarget UserWithRolesResponseDto bookDto, User user) {
        bookDto.setRoleIds(user.getRoles()
                .stream()
                .map(Role::getId)
                .collect(Collectors.toSet())
        );
    }
}

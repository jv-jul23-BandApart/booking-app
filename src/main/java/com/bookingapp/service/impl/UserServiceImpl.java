package com.bookingapp.service.impl;

import com.bookingapp.dto.user.UserRegistrationRequestDto;
import com.bookingapp.dto.user.UserResponseDto;
import com.bookingapp.dto.user.UserRolesUpdateRequestDto;
import com.bookingapp.dto.user.UserUpdateRequestDto;
import com.bookingapp.dto.user.UserWithRolesResponseDto;
import com.bookingapp.exception.ChangingRoleException;
import com.bookingapp.exception.EntityNotFoundException;
import com.bookingapp.exception.RegistrationException;
import com.bookingapp.mapper.UserMapper;
import com.bookingapp.model.Role;
import com.bookingapp.model.User;
import com.bookingapp.repository.RoleRepository;
import com.bookingapp.repository.UserRepository;
import com.bookingapp.service.UserService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.email())) {
            throw new RegistrationException("User with this email is already registered: "
                    + requestDto.email());
        }
        User user = userMapper.toUser(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.password()));
        user.setRoles(getDefaultRole());
        User savedUser = userRepository.save(user);
        return userMapper.toUserResponse(savedUser);
    }
    
    @Override
    public UserResponseDto getCurrentUserProfileInfo() {
        return userMapper.toUserResponse(getAuthenticatedUser());
    }
    
    @Override
    public UserResponseDto updateCurrentUserProfileInfo(UserUpdateRequestDto requestDto) {
        User user = getAuthenticatedUser();
        userMapper.mapUpdateRequestToUser(requestDto, user);
        return userMapper.toUserResponse(userRepository.save(user));
    }
    
    @Override
    public UserWithRolesResponseDto updateUserRoles(Long id, UserRolesUpdateRequestDto requestDto) {
        User user = userRepository.findUserById(id).orElseThrow(
                () -> new EntityNotFoundException("""
                        User roles update operation is failed
                        There is no user in the DB with id: %d""".formatted(id))
        );
        setUserRoles(requestDto.getRoleIds(), user);
        return userMapper.toDtoWithRoles(userRepository.save(user));
    }
    
    private Set<Role> getDefaultRole() {
        return new HashSet<>(List.of(
                roleRepository.findByName(Role.RoleName.USER)));
    }
    
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findUserByEmail(userDetails.getUsername()).get();
    }
    
    private void setUserRoles(Set<Long> rolesId, User user) {
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(rolesId));
        if (!roles.isEmpty()) {
            user.setRoles(roles);
        } else {
            throw new ChangingRoleException("There is no roles with such ids: " + rolesId);
        }
    }
}

package com.bookingapp.service.impl;

import com.bookingapp.dto.user.UserRegistrationRequestDto;
import com.bookingapp.dto.user.UserResponseDto;
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

    private Set<Role> getDefaultRole() {
        return new HashSet<>(List.of(
                roleRepository.findByName(Role.RoleName.USER)));
    }
}

package com.bookingapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.bookingapp.dto.user.UserRegistrationRequestDto;
import com.bookingapp.dto.user.UserResponseDto;
import com.bookingapp.exception.RegistrationException;
import com.bookingapp.mapper.UserMapper;
import com.bookingapp.model.Role;
import com.bookingapp.model.User;
import com.bookingapp.repository.RoleRepository;
import com.bookingapp.repository.UserRepository;
import com.bookingapp.service.impl.UserServiceImpl;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Throw exception by trying to register existing user")
    void registerUser_userAlreadyExists_throwRegistrationException() {
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto("test@example.com",
                "q1234","q1234", "firstName", "lastName");

        when(userRepository.existsByEmail(requestDto.email())).thenReturn(true);

        assertThrows(RegistrationException.class, () -> userService.register(requestDto));
    }

    @Test
    @DisplayName("Register new user with valid register data")
    void registerUser_WithValidRegisterData_Ok() {
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto("test@example.com",
                "q1234","q1234", "firstName", "lastName");

        Role testUserRole = new Role();
        testUserRole.setName(Role.RoleName.valueOf("USER"));

        User user = userMapper.toUser(requestDto);
        user.setRoles(Set.of(testUserRole));
        user.setPassword(passwordEncoder.encode("q1234"));
        when(userRepository.existsByEmail(requestDto.email())).thenReturn(false);
        when(roleRepository.findByName(Role.RoleName.USER)).thenReturn(testUserRole);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUser(requestDto)).thenReturn(user);

        UserResponseDto responseDto = userService.register(requestDto);

        assertEquals("test@example.com",responseDto.email());
        assertEquals("firstName",responseDto.firstName());
        assertEquals("lastName",responseDto.lastName());
    }
}

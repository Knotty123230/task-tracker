package com.example.tasktracker.service;

import com.example.tasktracker.dto.RegistrationRequest;
import com.example.tasktracker.entity.User;
import com.example.tasktracker.mapper.UserMapper;
import com.example.tasktracker.repository.UserRepository;
import com.example.tasktracker.utils.SecurityUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;
    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserWithAuthorities() {
        // Arrange
        try (var mocked = Mockito.mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getCurrentUsername).thenReturn(Optional.of("user1"));
            when(userRepository.findOneWithAuthoritiesByUsername("user1")).thenReturn(Optional.of(new User()));

            // Act
            Optional<User> user = userService.getUserWithAuthorities();

            // Assert
            assertTrue(user.isPresent());
        }
    }

    @Test
    void testSave() {
        // Arrange
        RegistrationRequest registrationRequest = getRegistrationRequest();
        User user = new User();
        when(userMapper.registrationDtoToUser(registrationRequest)).thenReturn(user);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User savedUser = userService.save(registrationRequest);

        // Assert
        assertNotNull(savedUser);
        assertEquals("ROLE_USER", savedUser.getAuthorities().iterator().next().getName());
        assertTrue(savedUser.isActivated());
        assertEquals("encodedPassword", savedUser.getPassword());
    }

    @NotNull
    private  RegistrationRequest getRegistrationRequest() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("username");
        registrationRequest.setEmail("email@email.com");
        registrationRequest.setPassword("password");
        registrationRequest.setFirstName("first name");
        registrationRequest.setLastName("last name");
        return registrationRequest;
    }
}
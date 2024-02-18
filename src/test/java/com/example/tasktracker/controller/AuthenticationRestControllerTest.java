package com.example.tasktracker.controller;

import com.example.tasktracker.dto.LoginDto;
import com.example.tasktracker.dto.RegistrationRequest;
import com.example.tasktracker.entity.User;
import com.example.tasktracker.jwt.TokenProvider;
import com.example.tasktracker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
class AuthenticationRestControllerTest {

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @InjectMocks
    private AuthenticationRestController authenticationRestController;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);
        // Assume authenticationManagerBuilder is already injected or instantiated somewhere in your test class
        Mockito.when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);

        // Mock the authentication process if necessary
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
    }

    @Test
    void testAuthorize() {
        // Arrange
        LoginDto loginDto = Mockito.mock(LoginDto.class);
        Mockito.when(loginDto.getUsername()).thenReturn("username");
        Mockito.when(loginDto.getPassword()).thenReturn("password");
        Mockito.when(loginDto.isRememberMe()).thenReturn(true);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authenticationManagerBuilder.getObject().authenticate(authenticationToken)).thenReturn(authentication);


        Mockito.when(tokenProvider.createToken(authentication, true)).thenReturn("mockedToken");

        // Act
        ResponseEntity<AuthenticationRestController.JWTToken> response = authenticationRestController.authorize(loginDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("mockedToken", response.getBody().idToken());
    }

    @Test
    void testRegistration() {
        // Arrange
        RegistrationRequest registrationRequest = new RegistrationRequest("username", "password");
        User savedUser = new User();
        savedUser.setUsername("username");
        Mockito.when(userService.save(registrationRequest)).thenReturn(savedUser);

        // Act
        ResponseEntity<String> response = authenticationRestController.registration(registrationRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("user successfully registered: username - username", response.getBody());
    }
}
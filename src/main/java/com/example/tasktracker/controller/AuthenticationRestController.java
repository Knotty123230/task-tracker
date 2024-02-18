package com.example.tasktracker.controller;

import com.example.tasktracker.dto.LoginDto;
import com.example.tasktracker.dto.RegistrationRequest;
import com.example.tasktracker.entity.User;
import com.example.tasktracker.jwt.JWTFilter;
import com.example.tasktracker.jwt.TokenProvider;
import com.example.tasktracker.service.UserService;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to authenticate users.
 */
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")

public class AuthenticationRestController {

    private final TokenProvider tokenProvider;
    private final UserService userService;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthenticationRestController(TokenProvider tokenProvider, UserService userService, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.userService = userService;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginDto loginDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        boolean rememberMe = loginDto.isRememberMe() != null && loginDto.isRememberMe();
        String jwt = tokenProvider.createToken(authentication, rememberMe);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody RegistrationRequest registrationRequest) {
        User save = userService.save(registrationRequest);
        return ResponseEntity.ok("user successfully registered: username - %s".formatted(save.getUsername()));
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    record JWTToken(String idToken) {

        @Override
        @JsonProperty("id_token")
        public String idToken() {
            return idToken;
        }
    }
}

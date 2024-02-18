package com.example.tasktracker.service;

import com.example.tasktracker.entity.Authority;
import com.example.tasktracker.entity.User;
import com.example.tasktracker.exception.UserNotActivatedException;
import com.example.tasktracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserModelDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserModelDetailsService userDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        Authority authority = new Authority();
        authority.setName("ROLE_USER");
        user = new User();
        user.setUsername("testUser");
        user.setPassword("password");
        user.setActivated(true);
        user.setAuthorities(Set.of(authority));
    }

    @Test
    void loadUserByUsername_whenUserExists_thenSucceeds() {
        when(userRepository.findOneWithAuthoritiesByUsername("testuser")).thenReturn(Optional.of(user));

        var userDetails = userDetailsService.loadUserByUsername("testUser");

        assertThat(userDetails.getUsername()).isEqualTo("testUser");
        assertThat(userDetails.getAuthorities()).hasSize(1);
        verify(userRepository, times(1)).findOneWithAuthoritiesByUsername("testuser");
    }


    @Test
    void loadUserByUsername_whenUserDoesNotExist_thenThrowsUsernameNotFoundException() {
        when(userRepository.findOneWithAuthoritiesByUsername("unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("unknown"))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void loadUserByUsername_whenUserIsNotActivated_thenThrowsUserNotActivatedException() {
        user.setActivated(false);
        when(userRepository.findOneWithAuthoritiesByUsername("testuser")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("testUser"))
                .isInstanceOf(UserNotActivatedException.class);
    }


}

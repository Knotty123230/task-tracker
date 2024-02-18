package com.example.tasktracker.jwt;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class JWTFilterTest {

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JWTFilter jwtFilter;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }
    @Test
    void testFilterChainProceedsWithValidToken() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer validToken");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(tokenProvider.validateToken("validToken")).thenReturn(true);
        Authentication authentication = mock(Authentication.class);
        when(tokenProvider.getAuthentication(anyString())).thenReturn(authentication);

        jwtFilter.doFilter(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(tokenProvider, times(1)).getAuthentication("validToken");
        // Verify SecurityContext is populated
        Authentication authInContext = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authInContext);
    }

    @Test
    void testFilterChainProceedsWithInvalidToken() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer invalidToken");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(tokenProvider.validateToken("invalidToken")).thenReturn(false);

        jwtFilter.doFilter(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        // Verify SecurityContext is not populated
        Authentication authInContext = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authInContext);
    }
    @Test
    void testFilterChainProceedsWithoutToken() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtFilter.doFilter(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        // Verify SecurityContext is not populated
        Authentication authInContext = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authInContext);
    }



}

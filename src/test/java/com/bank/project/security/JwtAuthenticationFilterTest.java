package com.bank.project.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class JwtAuthenticationFilterTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtTokenProvider);
    }

    @Test
    void testDoFilterInternal_WithValidToken() throws ServletException, IOException {
        // Arrange
        String validToken = "valid-token";
        when(jwtTokenProvider.resolveToken(request)).thenReturn(validToken);
        when(jwtTokenProvider.validateToken(validToken)).thenReturn(true);
        when(jwtTokenProvider.getAuthentication(validToken))
                .thenReturn(new UsernamePasswordAuthenticationToken("user", null, new ArrayList<>()));  // Mocked authentication object

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtTokenProvider).resolveToken(request);
        verify(jwtTokenProvider).validateToken(validToken);
        verify(jwtTokenProvider).getAuthentication(validToken);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication(), "Authentication should be set.");
        verify(filterChain).doFilter(request, response);  // Ensure that the filter chain proceeds
    }


    @Test
    void testDoFilterInternal_WithInvalidToken() throws ServletException, IOException {
        // Arrange
        String invalidToken = "invalid-token";
        when(jwtTokenProvider.resolveToken(request)).thenReturn(invalidToken);
        when(jwtTokenProvider.validateToken(invalidToken)).thenReturn(false);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtTokenProvider).resolveToken(request);
        verify(jwtTokenProvider).validateToken(invalidToken);
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication(), "Authentication should not be set for invalid token.");
    }

    @Test
    void testDoFilterInternal_WithNoToken() throws ServletException, IOException {
        // Arrange
        when(jwtTokenProvider.resolveToken(request)).thenReturn(null);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtTokenProvider).resolveToken(request);
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication(), "Authentication should not be set when no token is provided.");
    }
}

package com.bank.project.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.AuthenticationException;
import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class JwtAuthenticationEntryPointTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationException authException;

    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtAuthenticationEntryPoint = new JwtAuthenticationEntryPoint();
    }

    @Test
    void testCommence() throws IOException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/test");
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        when(authException.getMessage()).thenReturn("Token is missing or invalid.");

        // Act
        jwtAuthenticationEntryPoint.commence(request, response, authException);

        // Assert
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Token is missing or invalid.");
    }

    @Test
    void testCommence_WhenAuthExceptionMessageIsNull() throws IOException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/test");
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        when(authException.getMessage()).thenReturn(null);

        // Act
        jwtAuthenticationEntryPoint.commence(request, response, authException);

        // Assert
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: null");
    }
}

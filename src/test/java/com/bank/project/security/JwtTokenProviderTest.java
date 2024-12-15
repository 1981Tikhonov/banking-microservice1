package com.bank.project.security;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    @Mock
    private HttpServletRequest request;

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtTokenProvider = new JwtTokenProvider();
    }

    @Test
    void testCreateToken() {
        // Arrange
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "password");

        // Act
        String token = jwtTokenProvider.createToken(authentication);

        // Assert
        assertNotNull(token, "Token should not be null.");
        assertTrue(token.startsWith("ey"), "Token should start with 'ey' indicating it's a valid JWT.");
    }

    @Test
    void testResolveToken_WithBearerToken() {
        // Arrange
        String bearerToken = "Bearer valid-jwt-token";
        when(request.getHeader("Authorization")).thenReturn(bearerToken);

        // Act
        String token = jwtTokenProvider.resolveToken(request);

        // Assert
        assertNotNull(token, "Token should not be null.");
        assertEquals("valid-jwt-token", token, "Token should match the resolved value.");
    }

    @Test
    void testResolveToken_WithNoBearerToken() {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader");

        // Act
        String token = jwtTokenProvider.resolveToken(request);

        // Assert
        assertNull(token, "Token should be null if Authorization header does not start with 'Bearer '.");
    }

    @Test
    void testValidateToken_WithValidToken() {
        // Arrange
        String validToken = jwtTokenProvider.createToken(new UsernamePasswordAuthenticationToken("user", "password"));

        // Act
        boolean isValid = jwtTokenProvider.validateToken(validToken);

        // Assert
        assertTrue(isValid, "Token should be valid.");
    }

    @Test
    void testValidateToken_WithInvalidToken() {
        // Arrange
        String invalidToken = "invalid-token";

        // Act
        boolean isValid = jwtTokenProvider.validateToken(invalidToken);

        // Assert
        assertFalse(isValid, "Token should be invalid.");
    }

    @Test
    void testGetAuthentication() {
        // Arrange
        String token = jwtTokenProvider.createToken(new UsernamePasswordAuthenticationToken("user", "password"));

        // Act
        Authentication authentication = jwtTokenProvider.getAuthentication(token);

        // Assert
        assertNotNull(authentication, "Authentication should not be null.");
        assertEquals("user", authentication.getName(), "Username should match the one from the token.");
    }

    @Test
    void testGetUsername() {
        // Arrange
        String token = jwtTokenProvider.createToken(new UsernamePasswordAuthenticationToken("user", "password"));

        // Act
        String username = jwtTokenProvider.getUsername(token);

        // Assert
        assertEquals("user", username, "Username should be extracted from the token.");
    }
}

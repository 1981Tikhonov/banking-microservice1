package com.bank.project.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // Log details of unauthorized access
        logger.error("Unauthorized access attempt detected.");
        logger.error("Request URI: {}", request.getRequestURI());
        logger.error("Remote address: {}", request.getRemoteAddr());
        logger.error("Error message: {}", authException.getMessage());

        // Send 401 Unauthorized response with detailed message
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: " + authException.getMessage());
    }

    public void commence(HttpServletRequest request, HttpServletResponse response, javax.naming.AuthenticationException authException) {
    }
}

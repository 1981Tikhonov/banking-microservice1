package com.bank.project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtTokenProvider jwtTokenProvider;

    // Constructor with dependency injection
    @Autowired
    public JwtAuthenticationFilter(@Lazy JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        logger.info("JwtTokenProvider injected: {}", jwtTokenProvider != null ? "Yes" : "No");
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(request);

        // Log the request details for debugging
        logger.debug("Processing request URI: {}", request.getRequestURI());
        logger.debug("HTTP Method: {}", request.getMethod());
        logger.debug("Remote Address: {}", request.getRemoteAddr());

        if (token != null) {
            logger.debug("JWT Token found in the request: {}", token);
            if (jwtTokenProvider.validateToken(token)) {
                logger.info("JWT token is valid, setting authentication context.");
                SecurityContextHolder.getContext().setAuthentication(jwtTokenProvider.getAuthentication(token));
            } else {
                logger.warn("JWT token validation failed.");
            }
        } else {
            logger.warn("No JWT token found in the request.");
        }

        // Proceed with the filter chain
        filterChain.doFilter(request, response);
    }
}

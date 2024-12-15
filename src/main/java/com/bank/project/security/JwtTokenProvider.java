package com.bank.project.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512); // Generate secure key

    // Generate JWT token based on authentication
    public String createToken(Authentication authentication) {
        User userPrincipal = (User) authentication.getPrincipal();
        long validityInMilliseconds = 3600000; // Token validity: 1 hour
        String token = Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validityInMilliseconds))
                .signWith(secretKey)
                .compact();

        logger.info("JWT token created for user: {}", userPrincipal.getUsername());
        return token;
    }

    // Extract token from the request header
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            logger.debug("JWT token resolved from request header.");
            return bearerToken.substring(7);
        }
        logger.warn("Authorization header is missing or does not start with 'Bearer '.");
        return null;
    }

    // Validate JWT token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            logger.info("JWT token validation successful.");
            return true;
        } catch (Exception e) {
            logger.error("JWT token validation failed: {}", e.getMessage());
            return false;
        }
    }

    // Retrieve authentication from token
    public Authentication getAuthentication(String token) {
        String username = getUsername(token);
        logger.debug("Extracted username from token: {}", username);
        User userPrincipal = new User(username, "", new ArrayList<>());
        return new UsernamePasswordAuthenticationToken(userPrincipal, token, new ArrayList<>());
    }

    // Extract username from token
    public String getUsername(String token) {
        String username = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        logger.debug("Username extracted from JWT token: {}", username);
        return username;
    }
}

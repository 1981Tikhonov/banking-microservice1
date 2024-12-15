package com.bank.project.controller;

import com.bank.project.security.JwtTokenProvider;
import com.bank.project.service.ManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for authentication and authorization")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final ManagerService managerService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, ManagerService managerService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.managerService = managerService;  // used to check manager's status
    }

    @Operation(summary = "Login", description = "Authenticates a manager and returns a JWT token if successful.")
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Parameter(description = "Username of the manager to log in") @RequestParam String username,
            @Parameter(description = "Password of the manager to log in") @RequestParam String password) {
        logger.info("Attempting to log in with username: {}", username);

        // Example using managerService to check if the manager is active
        if (!managerService.isManagerActive(username)) {
            logger.warn("Manager with username: {} is not active", username);
            return ResponseEntity.status(403).body("Manager is not active");
        }

        try {
            // Authentication process
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // Create JWT token
            String token = jwtTokenProvider.createToken(authentication);

            logger.info("Authentication successful for manager: {}", username);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            logger.error("Authentication failed for manager: {}", username, e);
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }
}

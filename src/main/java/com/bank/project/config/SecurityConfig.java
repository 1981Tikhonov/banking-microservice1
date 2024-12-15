package com.bank.project.config;

import com.bank.project.security.JwtAuthenticationEntryPoint;
import com.bank.project.security.JwtAuthenticationFilter;
import com.bank.project.service.ManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final ManagerService managerService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Constructor for SecurityConfig
    public SecurityConfig(@Lazy ManagerService managerService,
                          JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                          JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.managerService = managerService;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;

        logger.info("JwtAuthenticationEntryPoint injected: {}", jwtAuthenticationEntryPoint != null ? "Yes" : "No");
        logger.info("JwtAuthenticationFilter injected: {}", jwtAuthenticationFilter != null ? "Yes" : "No");
    }

    // Bean for password encoding
    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.debug("Creating BCryptPasswordEncoder bean.");
        return new BCryptPasswordEncoder();
    }

    // Bean for AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        logger.debug("Creating AuthenticationManager bean.");
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(managerService).passwordEncoder(passwordEncoder());
        logger.info("AuthenticationManager configured successfully.");
        return authenticationManagerBuilder.build();
    }

    // Bean for configuring SecurityFilterChain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring security filter chain.");

        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for stateless API
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/api/auth/login").permitAll() // Allow login endpoint
                        .requestMatchers("/actuator/**").permitAll() // Allow Actuator endpoints
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()  // Allow Swagger UI and OpenAPI docs
                        .anyRequest().authenticated() // Require authentication for all other endpoints
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint) // Handle authentication errors
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter

        logger.info("Security filter chain successfully configured.");
        return http.build();
    }
}

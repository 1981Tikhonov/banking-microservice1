package com.bank.project.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitConfig {

    // Rate limits configuration
    private static final int REQUESTS_PER_MINUTE = 100;
    private static final int REQUESTS_PER_HOUR = 1000;
    private static final int REQUESTS_PER_DAY = 5000;

    @Bean
    public Bucket apiRateLimiter() {
        // Refill tokens at a fixed rate
        Refill refill = Refill.intervally(REQUESTS_PER_MINUTE, Duration.ofMinutes(1));
        
        // Create a bandwidth (capacity and refill rate)
        Bandwidth limit = Bandwidth.classic(REQUESTS_PER_MINUTE, refill)
            .withInitialTokens(REQUESTS_PER_MINUTE);
        
        return Bucket4j.builder()
            .addLimit(limit)
            .build();
    }

    @Bean
    public Bucket authRateLimiter() {
        // Stricter limits for authentication endpoints
        return Bucket4j.builder()
            .addLimit(Bandwidth.simple(5, Duration.ofMinutes(1))) // 5 requests per minute
            .addLimit(Bandwidth.simple(20, Duration.ofHours(1)))  // 20 requests per hour
            .build();
    }

    @Bean
    public Bucket publicApiRateLimiter() {
        // More generous limits for public API endpoints
        return Bucket4j.builder()
            .addLimit(Bandwidth.simple(200, Duration.ofMinutes(1))) // 200 requests per minute
            .addLimit(Bandwidth.simple(2000, Duration.ofHours(1))) // 2000 requests per hour
            .build();
    }
}

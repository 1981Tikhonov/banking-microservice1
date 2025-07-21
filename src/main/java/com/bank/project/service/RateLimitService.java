package com.bank.project.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String path, String clientId) {
        String cacheKey = getCacheKey(path, clientId);
        return cache.computeIfAbsent(cacheKey, k -> createNewBucket(path));
    }

    private Bucket createNewBucket(String path) {
        // Default rate limits
        Bandwidth limit = Bandwidth.simple(100, Duration.ofMinutes(1));
        
        // Stricter limits for authentication endpoints
        if (path.contains("/api/auth/")) {
            limit = Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(1)));
        } 
        // Higher limits for public endpoints
        else if (path.contains("/api/public/")) {
            limit = Bandwidth.simple(200, Duration.ofMinutes(1));
        }
        // Specific limits for transaction endpoints
        else if (path.contains("/api/transactions/")) {
            limit = Bandwidth.simple(30, Duration.ofMinutes(1));
        }
        
        return Bucket4j.builder()
            .addLimit(limit)
            .build();
    }

    private String getCacheKey(String path, String clientId) {
        // Group similar endpoints for rate limiting
        String normalizedPath = path
            .replaceAll("\\d+", "{id}")
            .replaceAll("/[^/]+$", "/*");
            
        return clientId + "_" + normalizedPath;
    }
    
    public void clearCache() {
        cache.clear();
    }
}

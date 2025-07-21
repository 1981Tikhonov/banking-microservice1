package com.bank.project.security;

import com.bank.project.service.RateLimitService;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@Order(1)
public class RateLimitFilter implements Filter {

    private final RateLimitService rateLimitService;

    public RateLimitFilter(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, 
                        ServletResponse servletResponse, 
                        FilterChain filterChain) throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        // Get client IP or API key for rate limiting
        String clientId = getClientId(httpRequest);
        
        // Get the appropriate bucket based on the request path
        io.github.bucket4j.Bucket bucket = rateLimitService.resolveBucket(httpRequest.getRequestURI(), clientId);
        
        // Try to consume a token from the bucket
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        
        if (probe.isConsumed()) {
            // Add rate limit headers to the response
            httpResponse.addHeader("X-Rate-Limit-Remaining", 
                String.valueOf(probe.getRemainingTokens()));
            httpResponse.addHeader("X-Rate-Limit-Reset", 
                String.valueOf(TimeUnit.NANOSECONDS.toSeconds(probe.getNanosToWaitForRefill())));
            
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            // Return 429 Too Many Requests
            httpResponse.setContentType("application/json");
            httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            httpResponse.addHeader("X-Rate-Limit-Retry-After-Seconds", 
                String.valueOf(TimeUnit.NANOSECONDS.toSeconds(probe.getNanosToWaitForRefill())));
            
            httpResponse.getWriter().write(
                String.format("{\"error\":\"Rate limit exceeded. Try again in %d seconds\"}",
                    TimeUnit.NANOSECONDS.toSeconds(probe.getNanosToWaitForRefill()))
            );
        }
    }

    private String getClientId(HttpServletRequest request) {
        // First try to get API key from header
        String apiKey = request.getHeader("X-API-Key");
        if (apiKey != null && !apiKey.isEmpty()) {
            return "API_KEY_" + apiKey;
        }
        
        // Then try to get authenticated user
        String username = request.getUserPrincipal() != null ? 
            request.getUserPrincipal().getName() : null;
        if (username != null && !username.isEmpty()) {
            return "USER_" + username;
        }
        
        // Fall back to IP address
        return "IP_" + getClientIP(request);
    }

    private String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}

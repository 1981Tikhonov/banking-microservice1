package com.bank.project.config;

import com.bank.project.service.RateLimitService;
import jakarta.servlet.*;

import java.io.IOException;

public class RateLimitFilter implements Filter {
    public RateLimitFilter(RateLimitService rateLimitService) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
    }
}

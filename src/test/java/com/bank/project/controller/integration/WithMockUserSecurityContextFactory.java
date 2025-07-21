package com.bank.project.controller.integration;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WithMockUserSecurityContextFactory implements com.bank.project.controller.integration.WithSecurityContextFactory<WithMockUser> {
    
    @Override
    public SecurityContext createSecurityContext(WithMockUser withUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        
        List<SimpleGrantedAuthority> authorities = Arrays.stream(withUser.roles())
                .map(role -> new SimpleGrantedAuthority("RO_" + role))
                .collect(Collectors.toList());
        
        Authentication auth = new UsernamePasswordAuthenticationToken(
                withUser.username(),
                withUser.password(),
                authorities);
                
        context.setAuthentication(auth);
        return context;
    }
}

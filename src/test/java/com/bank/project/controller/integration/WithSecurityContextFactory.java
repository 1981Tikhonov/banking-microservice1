package com.bank.project.controller.integration;

import org.springframework.security.core.context.SecurityContext;

public interface WithSecurityContextFactory<T> {
    SecurityContext createSecurityContext(WithMockUser withUser);
}

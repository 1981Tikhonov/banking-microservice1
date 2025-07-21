package com.bank.project.controller.integration;

public @interface WithSecurityContext {
    Class<WithMockUserSecurityContextFactory> factory();
}

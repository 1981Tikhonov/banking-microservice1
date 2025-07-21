package com.bank.project.controller.integration;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
@com.bank.project.controller.integration.WithSecurityContext(factory = WithMockUserSecurityContextFactory.class)
public @interface WithMockUser {
    String username() default "testuser";
    String[] roles() default {"USER"};
    String password() default "password";
}

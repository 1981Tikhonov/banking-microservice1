package com.bank.project.config;

public @interface WithMockUser {
    String roles(); // default is "USER"
    String username() default "testuser";
    String password() default "password";
}

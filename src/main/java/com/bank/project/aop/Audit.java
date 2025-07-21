package com.bank.project.aop;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Audit {
    String action() default "";
    String entityType() default "";
    boolean logParameters() default true;
    boolean logResult() default true;
}

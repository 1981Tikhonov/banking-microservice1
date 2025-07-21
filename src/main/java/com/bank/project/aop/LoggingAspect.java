package com.bank.project.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    /**
     * Логирует выполнение методов в контроллерах
     */
    @Around("execution(* com.bank.project.controller..*(..))")
    public Object logControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        
        // Получаем информацию о методе
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        
        log.info("Вызов метода: {}.{}() с аргументами: {}", 
                className, methodName, joinPoint.getArgs());
        
        try {
            Object result = joinPoint.proceed();
            log.info("Метод: {}.{}() выполнен успешно", className, methodName);
            return result;
        } catch (Exception e) {
            log.error("Ошибка в методе: {}.{}() - {}", className, methodName, e.getMessage());
            throw e;
        }
    }

    /**
     * Логирует выполнение методов в сервисах
     */
    @Around("execution(* com.bank.project.service..*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        
        log.debug("Сервисный метод: {}.{}() вызван с аргументами: {}", 
                className, methodName, joinPoint.getArgs());
        
        try {
            Object result = joinPoint.proceed();
            log.debug("Сервисный метод: {}.{}() завершен успешно", className, methodName);
            return result;
        } catch (Exception e) {
            log.error("Ошибка в сервисном методе: {}.{}() - {}", 
                    className, methodName, e.getMessage(), e);
            throw e;
        }
    }
}

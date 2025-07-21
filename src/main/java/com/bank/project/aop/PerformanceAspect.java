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
public class PerformanceAspect {

    /**
     * Мониторинг производительности сервисных методов
     */
    @Around("execution(* com.bank.project.service..*(..))")
    public Object profileServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        
        log.debug("Запуск мониторинга производительности для метода: {}.{}", className, methodName);
        
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        
        try {
            return joinPoint.proceed();
        } finally {
            stopWatch.stop();
            long executionTime = stopWatch.getTotalTimeMillis();
            
            if (executionTime > 1000) {
                log.warn("Метод {}.{} выполняется медленно: {} мс", 
                        className, methodName, executionTime);
            } else {
                log.debug("Метод {}.{} выполнен за: {} мс", 
                        className, methodName, executionTime);
            }
        }
    }

    /**
     * Мониторинг производительности запросов к БД
     */
    @Around("execution(* com.bank.project.repository..*(..))")
    public Object profileRepositoryMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        
        try {
            return joinPoint.proceed();
        } finally {
            stopWatch.stop();
            long executionTime = stopWatch.getTotalTimeMillis();
            
            if (executionTime > 500) {
                log.warn("Медленный запрос к БД: {}.{} выполнен за {} мс", 
                        className, methodName, executionTime);
            }
        }
    }
}

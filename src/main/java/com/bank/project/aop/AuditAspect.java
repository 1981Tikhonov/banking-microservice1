package com.bank.project.aop;

import com.bank.project.entity.AuditLog;
import com.bank.project.repository.AuditLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    @AfterReturning(
        value = "@annotation(audit) && execution(* com.bank.project..*.*(..))",
        returning = "result"
    )
    public void auditSuccess(JoinPoint joinPoint, Audit audit, Object result) {
        try {
            if (audit.logResult()) {
                String username = getCurrentUsername();
                String action = audit.action().isEmpty() 
                    ? getDefaultAction(joinPoint) 
                    : audit.action();
                
                AuditLog auditLog = new AuditLog();
                auditLog.setUsername(username);
                auditLog.setAction(action);
                auditLog.setEntityType(audit.entityType());
                auditLog.setStatus("SUCCESS");
                auditLog.setIpAddress(getClientIP());
                
                // Log method parameters if enabled
                if (audit.logParameters()) {
                    String parameters = extractParameters(joinPoint);
                    auditLog.setNewValue(parameters);
                }
                
                // Log result if it's not void
                if (result != null && !result.getClass().equals(Void.TYPE)) {
                    try {
                        String resultJson = objectMapper.writeValueAsString(result);
                        auditLog.setNewValue(resultJson);
                    } catch (Exception e) {
                        log.warn("Failed to serialize result for audit log", e);
                    }
                }
                
                auditLogRepository.save(auditLog);
            }
        } catch (Exception e) {
            log.error("Error in audit logging", e);
        }
    }

    @AfterThrowing(
        value = "@annotation(audit) && execution(* com.bank.project..*.*(..))",
        throwing = "ex"
    )
    public void auditFailure(JoinPoint joinPoint, Audit audit, Exception ex) {
        try {
            String username = getCurrentUsername();
            String action = audit.action().isEmpty() 
                ? getDefaultAction(joinPoint) 
                : audit.action();
            
            AuditLog auditLog = new AuditLog();
            auditLog.setUsername(username);
            auditLog.setAction(action);
            auditLog.setEntityType(audit.entityType());
            auditLog.setStatus("FAILED");
            auditLog.setErrorMessage(ex.getMessage());
            auditLog.setIpAddress(getClientIP());
            
            // Log method parameters
            if (audit.logParameters()) {
                String parameters = extractParameters(joinPoint);
                auditLog.setNewValue(parameters);
            }
            
            auditLogRepository.save(auditLog);
        } catch (Exception e) {
            log.error("Error in failure audit logging", e);
        }
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication != null) ? authentication.getName() : "anonymous";
    }

    private String getClientIP() {
        try {
            HttpServletRequest request = 
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                    .getRequest();
            return request.getRemoteAddr();
        } catch (Exception e) {
            return "unknown";
        }
    }

    private String getDefaultAction(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return String.format("%s.%s", 
            joinPoint.getTarget().getClass().getSimpleName(),
            signature.getMethod().getName());
    }

    private String extractParameters(JoinPoint joinPoint) {
        try {
            Map<String, Object> params = new HashMap<>();
            String[] paramNames = ((MethodSignature) joinPoint.getSignature())
                .getParameterNames();
            Object[] paramValues = joinPoint.getArgs();
            
            if (paramNames != null) {
                for (int i = 0; i < paramNames.length; i++) {
                    // Skip sensitive data
                    if (!isSensitiveParameter(paramNames[i])) {
                        params.put(paramNames[i], paramValues[i]);
                    }
                }
            }
            
            return objectMapper.writeValueAsString(params);
        } catch (Exception e) {
            log.warn("Failed to extract parameters for audit log", e);
            return "{}";
        }
    }

    private boolean isSensitiveParameter(String paramName) {
        String lowerParam = paramName.toLowerCase();
        return lowerParam.contains("password") || 
               lowerParam.contains("secret") ||
               lowerParam.contains("token");
    }
}

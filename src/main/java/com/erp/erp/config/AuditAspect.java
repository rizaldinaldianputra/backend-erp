package com.erp.erp.config;

import com.erp.erp.model.AuditLog;
import com.erp.erp.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final AuditLogService auditLogService;

    @Pointcut("execution(* com.erp.erp.service.*Service.create*(..))")
    public void createMethods() {}

    @Pointcut("execution(* com.erp.erp.service.*Service.update*(..))")
    public void updateMethods() {}

    @Pointcut("execution(* com.erp.erp.service.*Service.delete*(..))")
    public void deleteMethods() {}

    @AfterReturning(pointcut = "createMethods()", returning = "result")
    public void logCreate(JoinPoint joinPoint, Object result) {
        logAction(joinPoint, result, AuditLog.Action.CREATE);
    }

    @AfterReturning(pointcut = "updateMethods()", returning = "result")
    public void logUpdate(JoinPoint joinPoint, Object result) {
        logAction(joinPoint, result, AuditLog.Action.UPDATE);
    }

    @AfterReturning(pointcut = "deleteMethods()")
    public void logDelete(JoinPoint joinPoint) {
        // For delete, usually it's void or doesn't return the entity.
        // We might need to get the ID from arguments.
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof Long) {
            String entityName = joinPoint.getTarget().getClass().getSimpleName().replace("Service", "");
            auditLogService.logAction(entityName, (Long) args[0], AuditLog.Action.DELETE, null, null);
        }
    }

    private void logAction(JoinPoint joinPoint, Object result, AuditLog.Action action) {
        try {
            if (result == null) return;

            String entityName = result.getClass().getSimpleName();
            Long entityId = null;

            // Try to get getId() via reflection
            try {
                Method getIdMethod = result.getClass().getMethod("getId");
                entityId = (Long) getIdMethod.invoke(result);
            } catch (Exception e) {
                log.warn("Could not get ID for entity {} in AuditAspect", entityName);
            }

            if (entityId != null) {
                auditLogService.logAction(entityName, entityId, action, null, result.toString());
            }
        } catch (Exception e) {
            log.error("Error in AuditAspect: {}", e.getMessage());
        }
    }
}

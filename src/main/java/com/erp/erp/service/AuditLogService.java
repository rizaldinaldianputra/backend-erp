package com.erp.erp.service;

import com.erp.erp.model.AuditLog;
import com.erp.erp.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public AuditLog logAction(String entityName, Long entityId, AuditLog.Action action, String oldValue,
            String newValue) {
        String currentUser = com.erp.erp.security.SecurityUtil.getCurrentUsername();
        AuditLog log = AuditLog.builder()
                .entityName(entityName)
                .entityId(entityId)
                .action(action)
                .oldValue(oldValue)
                .newValue(newValue)
                .performedBy(currentUser != null ? currentUser : "system")
                .performedAt(LocalDateTime.now())
                .build();
        return auditLogRepository.save(log);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> getLogsForEntity(String entityName, Long entityId) {
        return auditLogRepository.findByEntityNameAndEntityId(entityName, entityId);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> getLogsByUser(String username) {
        return auditLogRepository.findByPerformedBy(username);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }
}

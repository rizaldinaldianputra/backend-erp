package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.model.AuditLog;
import com.erp.erp.service.AuditLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
@Tag(name = "Audit Log", description = "Audit Log Tracking API")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping("/{entityName}/{entityId}")
    public ResponseEntity<ApiResponseDto<List<AuditLog>>> getLogsForEntity(
            @PathVariable String entityName,
            @PathVariable Long entityId) {

        List<AuditLog> logs = auditLogService.getLogsForEntity(entityName, entityId);
        return ResponseEntity.ok(ApiResponseDto.<List<AuditLog>>builder()
                .status("success")
                .message("Retrieved audit logs successfully")
                .data(logs)
                .build());
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<ApiResponseDto<List<AuditLog>>> getLogsByUser(@PathVariable String username) {
        List<AuditLog> logs = auditLogService.getLogsByUser(username);
        return ResponseEntity.ok(ApiResponseDto.<List<AuditLog>>builder()
                .status("success")
                .message("Retrieved audit logs for user: " + username)
                .data(logs)
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<AuditLog>>> getAllLogs() {
        List<AuditLog> logs = auditLogService.getAllLogs();
        return ResponseEntity.ok(ApiResponseDto.<List<AuditLog>>builder()
                .status("success")
                .message("Retrieved all audit logs successfully")
                .data(logs)
                .build());
    }
}

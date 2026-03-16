package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.dto.system.ApprovalConfigRequest;
import com.erp.erp.dto.system.ApprovalConfigResponse;
import com.erp.erp.service.ApprovalConfigurationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system/approval-configs")
@RequiredArgsConstructor
@Tag(name = "System - Approval Configuration", description = "Manage dynamic document approval rules")
public class ApprovalConfigurationController {

    private final ApprovalConfigurationService service;

    @PostMapping
    @Operation(summary = "Create an approval configuration")
    public ResponseEntity<ApiResponseDto<ApprovalConfigResponse>> createConfig(
            @RequestBody ApprovalConfigRequest request) {
        return ResponseEntity.ok(ApiResponseDto.<ApprovalConfigResponse>builder()
                .status("success")
                .data(service.createConfig(request))
                .build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an approval configuration")
    public ResponseEntity<ApiResponseDto<ApprovalConfigResponse>> updateConfig(@PathVariable Long id,
            @RequestBody ApprovalConfigRequest request) {
        return ResponseEntity.ok(ApiResponseDto.<ApprovalConfigResponse>builder()
                .status("success")
                .data(service.updateConfig(id, request))
                .build());
    }

    @GetMapping
    @Operation(summary = "Get all approval configurations")
    public ResponseEntity<ApiResponseDto<List<ApprovalConfigResponse>>> getAllConfigs() {
        return ResponseEntity.ok(ApiResponseDto.<List<ApprovalConfigResponse>>builder()
                .status("success")
                .data(service.getAllConfigs())
                .build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an approval configuration by ID")
    public ResponseEntity<ApiResponseDto<ApprovalConfigResponse>> getConfigById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDto.<ApprovalConfigResponse>builder()
                .status("success")
                .data(service.getConfigById(id))
                .build());
    }

    @GetMapping("/by-document/{documentType}")
    @Operation(summary = "Get active approval configurations by document type")
    public ResponseEntity<ApiResponseDto<List<ApprovalConfigResponse>>> getByDocumentType(
            @PathVariable String documentType) {
        return ResponseEntity.ok(ApiResponseDto.<List<ApprovalConfigResponse>>builder()
                .status("success")
                .data(service.getByDocumentType(documentType))
                .build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an approval configuration")
    public ResponseEntity<ApiResponseDto<Void>> deleteConfig(@PathVariable Long id) {
        service.deleteConfig(id);
        return ResponseEntity.ok(ApiResponseDto.<Void>builder()
                .status("success")
                .message("Deleted successfully")
                .build());
    }
}

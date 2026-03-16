package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.dto.OrganizationResponse;
import com.erp.erp.model.Organization;
import com.erp.erp.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organizations")
@Tag(name = "Organization", description = "Manage company organization data")
public class OrganizationController {

        private final OrganizationService organizationService;

        public OrganizationController(OrganizationService organizationService) {
                this.organizationService = organizationService;
        }

        @GetMapping
        @Operation(summary = "Get all organizations")
        public ResponseEntity<ApiResponseDto<Page<OrganizationResponse>>> getAll(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {
                try {
                        Pageable pageable = PageRequest.of(page, size);
                        Page<OrganizationResponse> organizations = organizationService.findAll(pageable);
                        return ResponseEntity.ok(
                                        ApiResponseDto.<Page<OrganizationResponse>>builder()
                                                        .status("success")
                                                        .message("Organizations fetched successfully")
                                                        .data(organizations)
                                                        .build());
                } catch (Exception e) {
                        return ResponseEntity.status(500).body(
                                        ApiResponseDto.<Page<OrganizationResponse>>builder()
                                                        .status("error")
                                                        .message("Failed to fetch organizations: " + e.getMessage())
                                                        .data(null)
                                                        .build());
                }
        }

        @GetMapping("/{id}")
        @Operation(summary = "Get organization by ID")
        public ResponseEntity<ApiResponseDto<OrganizationResponse>> getById(@PathVariable Long id) {
                try {
                        return organizationService.findById(id)
                                        .map(org -> ResponseEntity.ok(
                                                        ApiResponseDto.<OrganizationResponse>builder()
                                                                        .status("success")
                                                                        .message("Organization fetched successfully")
                                                                        .data(org)
                                                                        .build()))
                                        .orElse(ResponseEntity.status(404).body(
                                                        ApiResponseDto.<OrganizationResponse>builder()
                                                                        .status("error")
                                                                        .message("Organization not found")
                                                                        .data(null)
                                                                        .build()));
                } catch (Exception e) {
                        return ResponseEntity.status(500).body(
                                        ApiResponseDto.<OrganizationResponse>builder()
                                                        .status("error")
                                                        .message("Failed to fetch organization: " + e.getMessage())
                                                        .data(null)
                                                        .build());
                }
        }

        @PostMapping
        @Operation(summary = "Create a new organization")
        public ResponseEntity<ApiResponseDto<OrganizationResponse>> create(@RequestBody Organization org) {
                try {
                        OrganizationResponse created = organizationService.create(org);
                        return ResponseEntity.ok(
                                        ApiResponseDto.<OrganizationResponse>builder()
                                                        .status("success")
                                                        .message("Organization created successfully")
                                                        .data(created)
                                                        .build());
                } catch (Exception e) {
                        return ResponseEntity.status(500).body(
                                        ApiResponseDto.<OrganizationResponse>builder()
                                                        .status("error")
                                                        .message("Failed to create organization: " + e.getMessage())
                                                        .data(null)
                                                        .build());
                }
        }

        @PutMapping("/{id}")
        @Operation(summary = "Update organization by ID")
        public ResponseEntity<ApiResponseDto<OrganizationResponse>> update(
                        @PathVariable Long id,
                        @RequestBody Organization org) {
                try {
                        OrganizationResponse updated = organizationService.update(id, org);
                        return ResponseEntity.ok(
                                        ApiResponseDto.<OrganizationResponse>builder()
                                                        .status("success")
                                                        .message("Organization updated successfully")
                                                        .data(updated)
                                                        .build());
                } catch (Exception e) {
                        return ResponseEntity.status(500).body(
                                        ApiResponseDto.<OrganizationResponse>builder()
                                                        .status("error")
                                                        .message("Failed to update organization: " + e.getMessage())
                                                        .data(null)
                                                        .build());
                }
        }
}

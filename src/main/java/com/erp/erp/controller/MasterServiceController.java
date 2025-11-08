package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.dto.MasterServiceResponse;
import com.erp.erp.model.MasterService;
import com.erp.erp.service.MasterServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@Tag(name = "MasterService", description = "Manage services (master data)")
public class MasterServiceController {

    private final MasterServiceService masterServiceService;

    public MasterServiceController(MasterServiceService masterServiceService) {
        this.masterServiceService = masterServiceService;
    }

    @GetMapping
    @Operation(summary = "Get all services")
    public ResponseEntity<ApiResponseDto<List<MasterServiceResponse>>> getAll() {
        try {
            List<MasterServiceResponse> services = masterServiceService.findAll();
            return ResponseEntity.ok(ApiResponseDto.<List<MasterServiceResponse>>builder()
                    .status("success")
                    .message("Services fetched successfully")
                    .data(services)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponseDto.<List<MasterServiceResponse>>builder()
                    .status("error")
                    .message("Failed to fetch services: " + e.getMessage())
                    .data(null)
                    .build());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get service by ID")
    public ResponseEntity<ApiResponseDto<MasterServiceResponse>> getById(@PathVariable Long id) {
        try {
            return masterServiceService.findById(id)
                    .map(service -> ResponseEntity.ok(ApiResponseDto.<MasterServiceResponse>builder()
                            .status("success")
                            .message("Service fetched successfully")
                            .data(service)
                            .build()))
                    .orElse(ResponseEntity.status(404).body(ApiResponseDto.<MasterServiceResponse>builder()
                            .status("error")
                            .message("Service not found")
                            .data(null)
                            .build()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponseDto.<MasterServiceResponse>builder()
                    .status("error")
                    .message("Failed to fetch service: " + e.getMessage())
                    .data(null)
                    .build());
        }
    }

    @PostMapping
    @Operation(summary = "Create a new service")
    public ResponseEntity<ApiResponseDto<MasterServiceResponse>> create(@RequestBody MasterService masterService) {
        try {
            MasterService created = masterServiceService.create(masterService);
            return ResponseEntity.ok(ApiResponseDto.<MasterServiceResponse>builder()
                    .status("success")
                    .message("Service created successfully")
                    .data(masterServiceService.mapToResponse(created))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponseDto.<MasterServiceResponse>builder()
                    .status("error")
                    .message("Failed to create service: " + e.getMessage())
                    .data(null)
                    .build());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update service by ID")
    public ResponseEntity<ApiResponseDto<MasterServiceResponse>> update(@PathVariable Long id,
            @RequestBody MasterService masterService) {
        try {
            MasterServiceResponse updated = masterServiceService.update(id, masterService);
            return ResponseEntity.ok(ApiResponseDto.<MasterServiceResponse>builder()
                    .status("success")
                    .message("Service updated successfully")
                    .data(updated)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponseDto.<MasterServiceResponse>builder()
                    .status("error")
                    .message("Failed to update service: " + e.getMessage())
                    .data(null)
                    .build());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete service by ID")
    public ResponseEntity<ApiResponseDto<Void>> delete(@PathVariable Long id) {
        try {
            masterServiceService.delete(id);
            return ResponseEntity.ok(ApiResponseDto.<Void>builder()
                    .status("success")
                    .message("Service deleted successfully")
                    .data(null)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponseDto.<Void>builder()
                    .status("error")
                    .message("Failed to delete service: " + e.getMessage())
                    .data(null)
                    .build());
        }
    }
}

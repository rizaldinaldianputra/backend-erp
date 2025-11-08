package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.dto.WarehouseResponse;
import com.erp.erp.model.Warehouse;
import com.erp.erp.service.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
@Tag(name = "Warehouse", description = "Manage company warehouse data")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping
    @Operation(summary = "Get all warehouses")
    public ResponseEntity<ApiResponseDto<List<WarehouseResponse>>> getAll() {
        try {
            List<WarehouseResponse> warehouses = warehouseService.findAll();
            return ResponseEntity.ok(ApiResponseDto.<List<WarehouseResponse>>builder()
                    .status("success")
                    .message("Warehouses fetched successfully")
                    .data(warehouses)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponseDto.<List<WarehouseResponse>>builder()
                    .status("error")
                    .message("Failed to fetch warehouses: " + e.getMessage())
                    .data(null)
                    .build());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get warehouse by ID")
    public ResponseEntity<ApiResponseDto<WarehouseResponse>> getById(@PathVariable Long id) {
        try {
            return warehouseService.findById(id)
                    .map(wh -> ResponseEntity.ok(ApiResponseDto.<WarehouseResponse>builder()
                            .status("success")
                            .message("Warehouse fetched successfully")
                            .data(wh)
                            .build()))
                    .orElse(ResponseEntity.status(404).body(ApiResponseDto.<WarehouseResponse>builder()
                            .status("error")
                            .message("Warehouse not found")
                            .data(null)
                            .build()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponseDto.<WarehouseResponse>builder()
                    .status("error")
                    .message("Failed to fetch warehouse: " + e.getMessage())
                    .data(null)
                    .build());
        }
    }

    @GetMapping("/organization/{orgId}")
    @Operation(summary = "Get warehouses by organization ID")
    public ResponseEntity<ApiResponseDto<List<WarehouseResponse>>> getByOrganization(@PathVariable Long orgId) {
        try {
            List<WarehouseResponse> warehouses = warehouseService.findByOrganization(orgId);
            return ResponseEntity.ok(ApiResponseDto.<List<WarehouseResponse>>builder()
                    .status("success")
                    .message("Warehouses fetched successfully for organization ID " + orgId)
                    .data(warehouses)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponseDto.<List<WarehouseResponse>>builder()
                    .status("error")
                    .message("Failed to fetch warehouses: " + e.getMessage())
                    .data(null)
                    .build());
        }
    }

    @PostMapping
    @Operation(summary = "Create a new warehouse")
    public ResponseEntity<ApiResponseDto<WarehouseResponse>> create(@RequestBody Warehouse warehouse) {
        try {
            WarehouseResponse created = warehouseService.create(warehouse); // sekarang create mengembalikan
                                                                            // WarehouseResponse
            return ResponseEntity.ok(ApiResponseDto.<WarehouseResponse>builder()
                    .status("success")
                    .message("Warehouse created successfully")
                    .data(created)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponseDto.<WarehouseResponse>builder()
                    .status("error")
                    .message("Failed to create warehouse: " + e.getMessage())
                    .data(null)
                    .build());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update warehouse by ID")
    public ResponseEntity<ApiResponseDto<WarehouseResponse>> update(@PathVariable Long id,
            @RequestBody Warehouse warehouse) {
        try {
            WarehouseResponse updated = warehouseService.update(id, warehouse);
            return ResponseEntity.ok(ApiResponseDto.<WarehouseResponse>builder()
                    .status("success")
                    .message("Warehouse updated successfully")
                    .data(updated)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponseDto.<WarehouseResponse>builder()
                    .status("error")
                    .message("Failed to update warehouse: " + e.getMessage())
                    .data(null)
                    .build());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete warehouse by ID")
    public ResponseEntity<ApiResponseDto<Void>> delete(@PathVariable Long id) {
        try {
            warehouseService.delete(id);
            return ResponseEntity.ok(ApiResponseDto.<Void>builder()
                    .status("success")
                    .message("Warehouse deleted successfully")
                    .data(null)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponseDto.<Void>builder()
                    .status("error")
                    .message("Failed to delete warehouse: " + e.getMessage())
                    .data(null)
                    .build());
        }
    }
}

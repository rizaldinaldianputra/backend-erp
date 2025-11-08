package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.dto.OfficeResponse;
import com.erp.erp.model.Office;
import com.erp.erp.service.OfficeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offices")
@Tag(name = "Office", description = "Manage company office data")
public class OfficeController {

    private final OfficeService officeService;

    public OfficeController(OfficeService officeService) {
        this.officeService = officeService;
    }

    @GetMapping
    @Operation(summary = "Get all offices")
    public ResponseEntity<ApiResponseDto<List<OfficeResponse>>> getAll() {
        List<OfficeResponse> offices = officeService.findAllWithOrganization();
        return ResponseEntity.ok(ApiResponseDto.<List<OfficeResponse>>builder()
                .status("success")
                .message("Offices fetched successfully")
                .data(offices)
                .build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get office by ID")
    public ResponseEntity<ApiResponseDto<OfficeResponse>> getById(@PathVariable Long id) {
        return officeService.findById(id)
                .map(off -> ResponseEntity.ok(ApiResponseDto.<OfficeResponse>builder()
                        .status("success")
                        .message("Office fetched successfully")
                        .data(off)
                        .build()))
                .orElse(ResponseEntity.status(404).body(ApiResponseDto.<OfficeResponse>builder()
                        .status("error")
                        .message("Office not found")
                        .data(null)
                        .build()));
    }

    @GetMapping("/organization/{orgId}")
    @Operation(summary = "Get offices by organization ID")
    public ResponseEntity<ApiResponseDto<List<OfficeResponse>>> getByOrganization(@PathVariable Long orgId) {
        List<OfficeResponse> offices = officeService.findByOrganizationId(orgId);
        return ResponseEntity.ok(ApiResponseDto.<List<OfficeResponse>>builder()
                .status("success")
                .message("Offices fetched successfully for organization ID " + orgId)
                .data(offices)
                .build());
    }

    @PostMapping
    @Operation(summary = "Create a new office")
    public ResponseEntity<ApiResponseDto<OfficeResponse>> create(@RequestBody Office office) {
        Office created = officeService.create(office);
        return ResponseEntity.ok(ApiResponseDto.<OfficeResponse>builder()
                .status("success")
                .message("Office created successfully")
                .data(officeService.mapToResponse(created))
                .build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update office by ID")
    public ResponseEntity<ApiResponseDto<OfficeResponse>> update(@PathVariable Long id,
            @RequestBody Office office) {
        OfficeResponse updated = officeService.update(id, office);
        return ResponseEntity.ok(ApiResponseDto.<OfficeResponse>builder()
                .status("success")
                .message("Office updated successfully")
                .data(updated)
                .build());
    }

}

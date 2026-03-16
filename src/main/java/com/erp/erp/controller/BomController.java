package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.dto.manufacturing.BomRequest;
import com.erp.erp.dto.manufacturing.BomResponse;
import com.erp.erp.service.BomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boms")
@RequiredArgsConstructor
@Tag(name = "Manufacturing - BOM", description = "Manage Bill of Materials")
public class BomController {

    private final BomService bomService;

    @PostMapping
    @Operation(summary = "Create a new BOM")
    public ResponseEntity<ApiResponseDto<BomResponse>> createBom(@RequestBody BomRequest request) {
        BomResponse response = bomService.createBom(request);
        return ResponseEntity.ok(ApiResponseDto.<BomResponse>builder()
                .status("success")
                .data(response)
                .build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update BOM")
    public ResponseEntity<ApiResponseDto<BomResponse>> updateBom(@PathVariable Long id,
            @RequestBody BomRequest request) {
        BomResponse response = bomService.updateBom(id, request);
        return ResponseEntity.ok(ApiResponseDto.<BomResponse>builder()
                .status("success")
                .data(response)
                .build());
    }

    @GetMapping
    @Operation(summary = "Get all BOMs")
    public ResponseEntity<ApiResponseDto<Page<BomResponse>>> getAllBoms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponseDto.<Page<BomResponse>>builder()
                .status("success")
                .data(bomService.getAllBoms(pageable))
                .build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get BOM by ID")
    public ResponseEntity<ApiResponseDto<BomResponse>> getBomById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDto.<BomResponse>builder()
                .status("success")
                .data(bomService.getBomById(id))
                .build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete BOM")
    public ResponseEntity<ApiResponseDto<Void>> deleteBom(@PathVariable Long id) {
        bomService.deleteBom(id);
        return ResponseEntity.ok(ApiResponseDto.<Void>builder()
                .status("success")
                .message("BOM deleted successfully")
                .build());
    }
}

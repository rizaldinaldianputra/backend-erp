package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.dto.PurchaseRequestResponse;
import com.erp.erp.model.PurchaseRequest;
import com.erp.erp.service.PurchaseRequestService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchase-requests")
@Tag(name = "Purchase Request", description = "Manage company Purchase Request data")

public class PurchaseRequestController {

    private final PurchaseRequestService prService;

    public PurchaseRequestController(PurchaseRequestService prService) {
        this.prService = prService;
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<Page<PurchaseRequestResponse>>> getAll(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<PurchaseRequestResponse> response = prService.findAll(pageable);
        return ResponseEntity.ok(ApiResponseDto.<Page<PurchaseRequestResponse>>builder()
                .status("success")
                .message("Purchase requests fetched")
                .data(response)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<PurchaseRequestResponse>> getById(@PathVariable Long id) {
        PurchaseRequestResponse response = prService.findById(id);
        return ResponseEntity.ok(ApiResponseDto.<PurchaseRequestResponse>builder()
                .status("success")
                .data(response)
                .build());
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<PurchaseRequest>> create(@RequestBody PurchaseRequest pr) {
        PurchaseRequest saved = prService.create(pr);
        return ResponseEntity.ok(ApiResponseDto.<PurchaseRequest>builder()
                .status("success")
                .message("Purchase request created")
                .data(saved)
                .build());
    }

}

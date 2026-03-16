package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.dto.purchase.SupplierPaymentRequest;
import com.erp.erp.model.SupplierPayment;
import com.erp.erp.service.SupplierPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplier-payments")
@RequiredArgsConstructor
@Tag(name = "Purchasing - Supplier Payment", description = "Manage Supplier Payments")
public class SupplierPaymentController {

    private final SupplierPaymentService supplierPaymentService;

    @PostMapping
    @Operation(summary = "Create Supplier Payment")
    public ResponseEntity<ApiResponseDto<SupplierPayment>> createSupplierPayment(
            @RequestBody SupplierPaymentRequest request) {
        SupplierPayment payment = supplierPaymentService.createSupplierPayment(request);
        return ResponseEntity.ok(ApiResponseDto.<SupplierPayment>builder()
                .status("success")
                .data(payment)
                .build());
    }

    @GetMapping
    @Operation(summary = "Get all Supplier Payments")
    public ResponseEntity<ApiResponseDto<List<SupplierPayment>>> getAllSupplierPayments() {
        return ResponseEntity.ok(ApiResponseDto.<List<SupplierPayment>>builder()
                .status("success")
                .data(supplierPaymentService.getAllSupplierPayments())
                .build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Supplier Payment by ID")
    public ResponseEntity<ApiResponseDto<SupplierPayment>> getSupplierPaymentById(@PathVariable Long id) {
        return supplierPaymentService.getSupplierPaymentById(id)
                .map(payment -> ResponseEntity.ok(ApiResponseDto.<SupplierPayment>builder()
                        .status("success")
                        .data(payment)
                        .build()))
                .orElse(ResponseEntity.notFound().build());
    }
}

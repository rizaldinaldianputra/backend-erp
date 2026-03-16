package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.dto.purchase.PurchaseInvoiceRequest;
import com.erp.erp.model.PurchaseInvoice;
import com.erp.erp.service.PurchaseInvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-invoices")
@RequiredArgsConstructor
@Tag(name = "Purchasing - Invoice (Bill)", description = "Manage Purchase Invoices")
public class PurchaseInvoiceController {

    private final PurchaseInvoiceService purchaseInvoiceService;

    @PostMapping
    @Operation(summary = "Create Purchase Invoice from PO")
    public ResponseEntity<ApiResponseDto<PurchaseInvoice>> createPurchaseInvoice(
            @RequestBody PurchaseInvoiceRequest request) {
        PurchaseInvoice invoice = purchaseInvoiceService.createPurchaseInvoice(request);
        return ResponseEntity.ok(ApiResponseDto.<PurchaseInvoice>builder()
                .status("success")
                .data(invoice)
                .build());
    }

    @GetMapping
    @Operation(summary = "Get all Purchase Invoices")
    public ResponseEntity<ApiResponseDto<List<PurchaseInvoice>>> getAllPurchaseInvoices() {
        return ResponseEntity.ok(ApiResponseDto.<List<PurchaseInvoice>>builder()
                .status("success")
                .data(purchaseInvoiceService.getAllPurchaseInvoices())
                .build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Purchase Invoice by ID")
    public ResponseEntity<ApiResponseDto<PurchaseInvoice>> getPurchaseInvoiceById(@PathVariable Long id) {
        return purchaseInvoiceService.getPurchaseInvoiceById(id)
                .map(invoice -> ResponseEntity.ok(ApiResponseDto.<PurchaseInvoice>builder()
                        .status("success")
                        .data(invoice)
                        .build()))
                .orElse(ResponseEntity.notFound().build());
    }
}

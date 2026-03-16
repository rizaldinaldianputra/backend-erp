package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.dto.sales.QuotationRequest;
import com.erp.erp.dto.sales.QuotationResponse;
import com.erp.erp.model.Quotation;
import com.erp.erp.service.QuotationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/quotations")
@RequiredArgsConstructor
@Tag(name = "Sales - Quotation", description = "Manage sales quotations")
public class QuotationController {

    private final QuotationService quotationService;

    @PostMapping
    @Operation(summary = "Create a new quotation")
    public ResponseEntity<ApiResponseDto<QuotationResponse>> createQuotation(@RequestBody QuotationRequest request) {
        Quotation quotation = quotationService.createQuotation(request);
        return ResponseEntity.ok(ApiResponseDto.<QuotationResponse>builder()
                .status("success")
                .data(mapToResponse(quotation))
                .build());
    }

    @GetMapping
    @Operation(summary = "Get all quotations")
    public ResponseEntity<ApiResponseDto<List<QuotationResponse>>> getAllQuotations() {
        List<QuotationResponse> list = quotationService.getAllQuotations().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDto.<List<QuotationResponse>>builder()
                .status("success")
                .data(list)
                .build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get quotation by ID")
    public ResponseEntity<ApiResponseDto<QuotationResponse>> getQuotationById(@PathVariable Long id) {
        return quotationService.getQuotationById(id)
                .map(q -> ResponseEntity.ok(ApiResponseDto.<QuotationResponse>builder()
                        .status("success")
                        .data(mapToResponse(q))
                        .build()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update quotation status")
    public ResponseEntity<ApiResponseDto<QuotationResponse>> updateStatus(@PathVariable Long id,
            @RequestParam Quotation.Status status) {
        Quotation quotation = quotationService.updateStatus(id, status);
        return ResponseEntity.ok(ApiResponseDto.<QuotationResponse>builder()
                .status("success")
                .data(mapToResponse(quotation))
                .build());
    }

    private QuotationResponse mapToResponse(Quotation q) {
        return QuotationResponse.builder()
                .id(q.getId())
                .code(q.getCode())
                .customerId(q.getCustomer().getId())
                .customerName(q.getCustomer().getName())
                .date(q.getDate())
                .validUntil(q.getValidUntil())
                .note(q.getNote())
                .status(q.getStatus().name())
                .subTotal(q.getSubTotal())
                .taxAmount(q.getTaxAmount())
                .discountAmount(q.getDiscountAmount())
                .totalAmount(q.getTotalAmount())
                .items(q.getItems().stream().map(i -> QuotationResponse.QuotationItemResponse.builder()
                        .id(i.getId())
                        .productId(i.getProduct().getId())
                        .productName(i.getProduct().getName())
                        .quantity(i.getQuantity())
                        .price(i.getPrice())
                        .discount(i.getDiscount())
                        .total(i.getTotal())
                        .build()).collect(Collectors.toList()))
                .build();
    }
}

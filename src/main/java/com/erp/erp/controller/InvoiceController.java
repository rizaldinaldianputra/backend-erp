package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.dto.sales.InvoiceRequest;
import com.erp.erp.dto.sales.InvoiceResponse;
import com.erp.erp.model.Invoice;
import com.erp.erp.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
@Tag(name = "Sales - Invoice", description = "Manage sales invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    @Operation(summary = "Create a new invoice from sales order")
    public ResponseEntity<ApiResponseDto<InvoiceResponse>> createInvoice(@RequestBody InvoiceRequest request) {
        Invoice invoice = invoiceService.createInvoice(request);
        return ResponseEntity.ok(ApiResponseDto.<InvoiceResponse>builder()
                .status("success")
                .data(mapToResponse(invoice))
                .build());
    }

    @GetMapping
    @Operation(summary = "Get all invoices")
    public ResponseEntity<ApiResponseDto<List<InvoiceResponse>>> getAllInvoices() {
        List<InvoiceResponse> list = invoiceService.getAllInvoices().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDto.<List<InvoiceResponse>>builder()
                .status("success")
                .data(list)
                .build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get invoice by ID")
    public ResponseEntity<ApiResponseDto<InvoiceResponse>> getInvoiceById(@PathVariable Long id) {
        return invoiceService.getInvoiceById(id)
                .map(inv -> ResponseEntity.ok(ApiResponseDto.<InvoiceResponse>builder()
                        .status("success")
                        .data(mapToResponse(inv))
                        .build()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update invoice status")
    public ResponseEntity<ApiResponseDto<InvoiceResponse>> updateStatus(@PathVariable Long id,
            @RequestParam Invoice.Status status) {
        Invoice invoice = invoiceService.updateStatus(id, status);
        return ResponseEntity.ok(ApiResponseDto.<InvoiceResponse>builder()
                .status("success")
                .data(mapToResponse(invoice))
                .build());
    }

    private InvoiceResponse mapToResponse(Invoice inv) {
        return InvoiceResponse.builder()
                .id(inv.getId())
                .code(inv.getCode())
                .salesOrderId(inv.getSalesOrder().getId())
                .salesOrderCode(inv.getSalesOrder().getCode())
                .customerId(inv.getCustomer().getId())
                .customerName(inv.getCustomer().getName())
                .date(inv.getDate())
                .dueDate(inv.getDueDate())
                .status(inv.getStatus().name())
                .subTotal(inv.getSubTotal())
                .taxAmount(inv.getTaxAmount())
                .discountAmount(inv.getDiscountAmount())
                .totalAmount(inv.getTotalAmount())
                .paidAmount(inv.getPaidAmount())
                .remainingAmount(inv.getTotalAmount().subtract(inv.getPaidAmount()))
                .build();
    }
}

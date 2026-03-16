package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.dto.sales.PaymentRequest;
import com.erp.erp.dto.sales.PaymentResponse;
import com.erp.erp.model.Payment;
import com.erp.erp.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Sales - Payment", description = "Manage customer payments")
public class PaymentController {

        private final PaymentService paymentService;

        @PostMapping
        @Operation(summary = "Create a new payment")
        public ResponseEntity<ApiResponseDto<PaymentResponse>> createPayment(@RequestBody PaymentRequest request) {
                Payment payment = paymentService.createPayment(request);
                return ResponseEntity.ok(ApiResponseDto.<PaymentResponse>builder()
                                .status("success")
                                .data(mapToResponse(payment))
                                .build());
        }

        @GetMapping
        @Operation(summary = "Get all payments")
        public ResponseEntity<ApiResponseDto<List<PaymentResponse>>> getAllPayments() {
                List<PaymentResponse> list = paymentService.getAllPayments().stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());
                return ResponseEntity.ok(ApiResponseDto.<List<PaymentResponse>>builder()
                                .status("success")
                                .data(list)
                                .build());
        }

        @GetMapping("/{id}")
        @Operation(summary = "Get payment by ID")
        public ResponseEntity<ApiResponseDto<PaymentResponse>> getPaymentById(@PathVariable Long id) {
                return paymentService.getPaymentById(id)
                                .map(p -> ResponseEntity.ok(ApiResponseDto.<PaymentResponse>builder()
                                                .status("success")
                                                .data(mapToResponse(p))
                                                .build()))
                                .orElse(ResponseEntity.notFound().build());
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Delete payment (revert invoice status)")
        public ResponseEntity<ApiResponseDto<Void>> deletePayment(@PathVariable Long id) {
                paymentService.deletePayment(id);
                return ResponseEntity.ok(ApiResponseDto.<Void>builder()
                                .status("success")
                                .message("Payment deleted successfully")
                                .build());
        }

        private PaymentResponse mapToResponse(Payment p) {
                return PaymentResponse.builder()
                                .id(p.getId())
                                .code(p.getCode())
                                .invoiceId(p.getInvoice().getId())
                                .invoiceCode(p.getInvoice().getCode())
                                .customerId(p.getCustomer().getId())
                                .customerName(p.getCustomer().getName())
                                .date(p.getDate())
                                .paymentMethod(p.getPaymentMethod().name())
                                .amount(p.getAmount())
                                .note(p.getNote())
                                .receiptUrl(p.getReceiptUrl())
                                .build();
        }
}

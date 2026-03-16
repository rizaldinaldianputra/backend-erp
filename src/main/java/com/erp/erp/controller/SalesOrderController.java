package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.dto.sales.SalesOrderRequest;
import com.erp.erp.dto.sales.SalesOrderResponse;
import com.erp.erp.model.SalesOrder;
import com.erp.erp.service.SalesOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sales-orders")
@Tag(name = "Sales - Order", description = "Manage sales orders")
public class SalesOrderController {

        private final SalesOrderService salesOrderService;
        private final com.erp.erp.service.ReportService reportService;

        public SalesOrderController(SalesOrderService salesOrderService,
                        com.erp.erp.service.ReportService reportService) {
                this.salesOrderService = salesOrderService;
                this.reportService = reportService;
        }

        @PostMapping
        @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'SALES')")
        public ResponseEntity<ApiResponseDto<SalesOrderResponse>> createSalesOrder(
                        @RequestBody SalesOrderRequest request) {
                SalesOrder salesOrder = salesOrderService.createSalesOrder(request);
                return ResponseEntity.ok(ApiResponseDto.<SalesOrderResponse>builder()
                                .status("success")
                                .data(mapToResponse(salesOrder))
                                .build());
        }

        @PostMapping("/from-quotation/{quotationId}")
        @Operation(summary = "Create sales order from quotation")
        public ResponseEntity<ApiResponseDto<SalesOrderResponse>> createFromQuotation(@PathVariable Long quotationId) {
                SalesOrder salesOrder = salesOrderService.createSalesOrderFromQuotation(quotationId);
                return ResponseEntity.ok(ApiResponseDto.<SalesOrderResponse>builder()
                                .status("success")
                                .data(mapToResponse(salesOrder))
                                .build());
        }

        @GetMapping
        @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'SALES', 'WAREHOUSE')")
        public ResponseEntity<ApiResponseDto<List<SalesOrderResponse>>> getAllSalesOrders() {
                List<SalesOrderResponse> list = salesOrderService.getAllSalesOrders().stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());
                return ResponseEntity.ok(ApiResponseDto.<List<SalesOrderResponse>>builder()
                                .status("success")
                                .data(list)
                                .build());
        }

        @GetMapping("/{id}")
        @Operation(summary = "Get sales order by ID")
        public ResponseEntity<ApiResponseDto<SalesOrderResponse>> getSalesOrderById(@PathVariable Long id) {
                return salesOrderService.getSalesOrderById(id)
                                .map(so -> ResponseEntity.ok(ApiResponseDto.<SalesOrderResponse>builder()
                                                .status("success")
                                                .data(mapToResponse(so))
                                                .build()))
                                .orElse(ResponseEntity.notFound().build());
        }

        @PutMapping("/{id}/status")
        @Operation(summary = "Update sales order status")
        public ResponseEntity<ApiResponseDto<SalesOrderResponse>> updateStatus(@PathVariable Long id,
                        @RequestParam SalesOrder.Status status) {
                SalesOrder salesOrder = salesOrderService.updateStatus(id, status);
                return ResponseEntity.ok(ApiResponseDto.<SalesOrderResponse>builder()
                                .status("success")
                                .data(mapToResponse(salesOrder))
                                .build());
        }

        @GetMapping("/{id}/report")
        @Operation(summary = "Download Sales Order Report")
        public ResponseEntity<byte[]> downloadReport(@PathVariable Long id) {
                SalesOrder so = salesOrderService.getSalesOrderById(id)
                                .orElseThrow(() -> new RuntimeException("Sales Order not found"));

                try {
                        java.util.Map<String, Object> parameters = new java.util.HashMap<>();
                        parameters.put("soCode", so.getCode() != null ? so.getCode() : "");
                        parameters.put("customerName", so.getCustomer() != null ? so.getCustomer().getName() : "");
                        parameters.put("soDate", so.getDate() != null ? so.getDate().toString() : "");
                        parameters.put("deliveryDate",
                                        so.getDeliveryDate() != null ? so.getDeliveryDate().toString() : "");
                        parameters.put("note", so.getNote() != null ? so.getNote() : "");

                        List<java.util.Map<String, Object>> itemsData = new java.util.ArrayList<>();
                        if (so.getItems() != null) {
                                for (var item : so.getItems()) {
                                        java.util.Map<String, Object> map = new java.util.HashMap<>();
                                        map.put("productName",
                                                        item.getProduct() != null ? item.getProduct().getName() : "");
                                        map.put("quantity", item.getQuantity() != null ? item.getQuantity() : 0);

                                        java.math.BigDecimal price = item.getPrice() != null ? item.getPrice()
                                                        : java.math.BigDecimal.ZERO;
                                        java.math.BigDecimal discount = item.getDiscount() != null ? item.getDiscount()
                                                        : java.math.BigDecimal.ZERO;
                                        java.math.BigDecimal total = item.getTotal() != null ? item.getTotal()
                                                        : java.math.BigDecimal.ZERO;

                                        map.put("price", price);
                                        map.put("discount", discount);
                                        map.put("total", total);

                                        itemsData.add(map);
                                }
                        }

                        byte[] pdfBytes = reportService.generatePdfReport("reports/sales_order_detail_report.jrxml",
                                        parameters, itemsData);

                        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
                        headers.setContentType(org.springframework.http.MediaType.APPLICATION_PDF);
                        headers.setContentDispositionFormData("filename", "SO_" + so.getCode() + ".pdf");

                        return ResponseEntity.ok()
                                        .headers(headers)
                                        .body(pdfBytes);
                } catch (Exception e) {
                        e.printStackTrace();
                        return ResponseEntity.internalServerError().build();
                }
        }

        private SalesOrderResponse mapToResponse(SalesOrder so) {
                return SalesOrderResponse.builder()
                                .id(so.getId())
                                .code(so.getCode())
                                .quotationId(so.getQuotation() != null ? so.getQuotation().getId() : null)
                                .quotationCode(so.getQuotation() != null ? so.getQuotation().getCode() : null)
                                .customerId(so.getCustomer().getId())
                                .customerName(so.getCustomer().getName())
                                .date(so.getDate())
                                .deliveryDate(so.getDeliveryDate())
                                .note(so.getNote())
                                .status(so.getStatus().name())
                                .subTotal(so.getSubTotal())
                                .taxAmount(so.getTaxAmount())
                                .discountAmount(so.getDiscountAmount())
                                .totalAmount(so.getTotalAmount())
                                .items(so.getItems().stream()
                                                .map(i -> SalesOrderResponse.SalesOrderItemResponse.builder()
                                                                .id(i.getId())
                                                                .productId(i.getProduct().getId())
                                                                .productName(i.getProduct().getName())
                                                                .quantity(i.getQuantity())
                                                                .price(i.getPrice())
                                                                .discount(i.getDiscount())
                                                                .total(i.getTotal())
                                                                .build())
                                                .collect(Collectors.toList()))
                                .build();
        }
}

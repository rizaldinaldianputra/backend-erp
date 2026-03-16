package com.erp.erp.controller;

import com.erp.erp.dto.*;
import com.erp.erp.model.PurchaseOrder;
import com.erp.erp.model.PurchaseOrderItem;
import com.erp.erp.service.PurchaseOrderService;
import com.erp.erp.service.ProductService;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import com.erp.erp.model.Supplier;

@RestController
@RequestMapping("/api/purchase-orders")
@Tag(name = "Purchase Order", description = "Manage purchase orders")
public class PurchaseOrderController {

        private final PurchaseOrderService poService;
        private final ProductService productService;
        private final com.erp.erp.service.ReportService reportService;

        public PurchaseOrderController(PurchaseOrderService poService, ProductService productService,
                        com.erp.erp.service.ReportService reportService) {
                this.poService = poService;
                this.productService = productService;
                this.reportService = reportService;
        }

        // ================== MAP ITEM RESPONSE ==================
        private PurchaseOrderItemResponse mapItem(PurchaseOrderItem item) {
                return PurchaseOrderItemResponse.builder()
                                .id(item.getId())
                                .product(productService.toResponse(item.getProduct())) // ProductResponse
                                .quantity(item.getQuantity() != null ? item.getQuantity() : null)
                                .price(item.getPrice())
                                .discount(item.getDiscount()) // karena entity belum punya discount
                                .build();
        }

        // ================== MAP SUPPLIER ==================
        private SupplierResponse mapSupplier(Supplier supplier) {
                if (supplier == null)
                        return null;

                return SupplierResponse.builder()
                                .id(supplier.getId())
                                .code(supplier.getCode())
                                .name(supplier.getName())
                                .email(supplier.getEmail())
                                .phone(supplier.getPhone())
                                .address(supplier.getAddress())
                                .website(supplier.getWebsite())
                                .npwp(supplier.getNpwp())
                                .active(supplier.getActive())
                                .build();
        }

        // ================== MAP PO RESPONSE ==================
        private PurchaseOrderResponse mapToResponse(PurchaseOrder po) {
                return PurchaseOrderResponse.builder()
                                .id(po.getId())
                                .documentNumber(po.getDocumentNumber())
                                .trxDate(po.getTrxDate())
                                .notes(po.getNotes())
                                .status(po.getStatus())
                                .supplier(mapSupplier(po.getSupplier())) // ✅ sudah pakai SupplierResponse
                                .items(po.getItems() != null
                                                ? po.getItems().stream().map(this::mapItem).collect(Collectors.toList())
                                                : null)
                                .createdDate(po.getCreatedDate())
                                .updatedDate(po.getUpdatedDate())
                                .build();
        }

        // ================== GET ALL ==================
        @GetMapping
        // Temporarily removed @PreAuthorize for testing - TODO: Add back with proper role check
        public ResponseEntity<ApiResponseDto<List<PurchaseOrderResponse>>> getAll() {
                List<PurchaseOrderResponse> list = poService.getAll()
                                .stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());

                return ResponseEntity.ok(
                                ApiResponseDto.<List<PurchaseOrderResponse>>builder()
                                                .status("success")
                                                .message("Purchase orders fetched")
                                                .data(list)
                                                .build());
        }

        // ================== GET BY ID ==================
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponseDto<PurchaseOrderResponse>> getById(@PathVariable Long id) {
                return poService.getById(id)
                                .map(po -> ResponseEntity.ok(
                                                ApiResponseDto.<PurchaseOrderResponse>builder()
                                                                .status("success")
                                                                .message("Purchase order fetched")
                                                                .data(mapToResponse(po))
                                                                .build()))
                                .orElse(ResponseEntity.status(404).body(
                                                ApiResponseDto.<PurchaseOrderResponse>builder()
                                                                .status("error")
                                                                .message("Purchase order not found")
                                                                .data(null)
                                                                .build()));
        }

        // ================== CREATE ==================
        @PostMapping
        // Temporarily removed @PreAuthorize for testing - TODO: Add back with proper role check
        public ResponseEntity<ApiResponseDto<PurchaseOrderResponse>> create(@RequestBody PurchaseOrder po) {
                // Debug: Log authentication info
                org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
                if (auth != null) {
                        System.out.println("Current user: " + auth.getName());
                        System.out.println("Authorities: " + auth.getAuthorities());
                }
                
                PurchaseOrder created = poService.create(po);

                return ResponseEntity.ok(
                                ApiResponseDto.<PurchaseOrderResponse>builder()
                                                .status("success")
                                                .message("Purchase order created")
                                                .data(mapToResponse(created))
                                                .build());
        }

        // ================== UPDATE ==================
        @PutMapping("/{id}")
        @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'PURCHASING')")
        public ResponseEntity<ApiResponseDto<PurchaseOrderResponse>> update(
                        @PathVariable Long id,
                        @RequestBody PurchaseOrder po) {

                PurchaseOrder updated = poService.update(id, po);

                return ResponseEntity.ok(
                                ApiResponseDto.<PurchaseOrderResponse>builder()
                                                .status("success")
                                                .message("Purchase order updated")
                                                .data(mapToResponse(updated))
                                                .build());
        }

        // ================== DOWNLOAD REPORT ==================
        @GetMapping("/{id}/report")
        public ResponseEntity<byte[]> downloadReport(@PathVariable Long id) {
                PurchaseOrder po = poService.getById(id)
                                .orElseThrow(() -> new RuntimeException("Purchase Order not found"));

                try {
                        java.util.Map<String, Object> parameters = new java.util.HashMap<>();
                        parameters.put("documentNumber", po.getDocumentNumber() != null ? po.getDocumentNumber() : "");
                        parameters.put("trxDate", po.getTrxDate() != null ? po.getTrxDate().toString() : "");
                        parameters.put("supplierName", po.getSupplier() != null ? po.getSupplier().getName() : "");
                        parameters.put("notes", po.getNotes() != null ? po.getNotes() : "");

                        List<java.util.Map<String, Object>> itemsData = new java.util.ArrayList<>();
                        if (po.getItems() != null) {
                                for (PurchaseOrderItem item : po.getItems()) {
                                        java.util.Map<String, Object> map = new java.util.HashMap<>();
                                        map.put("productName",
                                                        item.getProduct() != null ? item.getProduct().getName() : "");
                                        map.put("quantity", item.getQuantity() != null ? item.getQuantity() : 0);

                                        java.math.BigDecimal price = item.getPrice() != null
                                                        ? java.math.BigDecimal.valueOf(item.getPrice())
                                                        : java.math.BigDecimal.ZERO;
                                        java.math.BigDecimal discount = item.getDiscount() != null
                                                        ? java.math.BigDecimal.valueOf(item.getDiscount())
                                                        : java.math.BigDecimal.ZERO;

                                        map.put("price", price);
                                        map.put("discount", discount);

                                        // subTotal = (price - discount) * quantity
                                        java.math.BigDecimal qtyBd = new java.math.BigDecimal(
                                                        item.getQuantity() != null ? item.getQuantity() : 0);
                                        java.math.BigDecimal subTotal = price.subtract(discount).multiply(qtyBd);
                                        map.put("subTotal", subTotal);

                                        itemsData.add(map);
                                }
                        }

                        byte[] pdfBytes = reportService.generatePdfReport("reports/purchase_order_detail_report.jrxml",
                                        parameters, itemsData);

                        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
                        headers.setContentType(org.springframework.http.MediaType.APPLICATION_PDF);
                        headers.setContentDispositionFormData("filename", "PO_" + po.getDocumentNumber() + ".pdf");

                        return ResponseEntity.ok()
                                        .headers(headers)
                                        .body(pdfBytes);
                } catch (Exception e) {
                        e.printStackTrace();
                        return ResponseEntity.internalServerError().build();
                }
        }
}

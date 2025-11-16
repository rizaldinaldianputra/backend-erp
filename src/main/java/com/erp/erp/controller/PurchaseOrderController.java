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

    public PurchaseOrderController(PurchaseOrderService poService, ProductService productService) {
        this.poService = poService;
        this.productService = productService;
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
    public ResponseEntity<ApiResponseDto<PurchaseOrderResponse>> create(@RequestBody PurchaseOrder po) {
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
}

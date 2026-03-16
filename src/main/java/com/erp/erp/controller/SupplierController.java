package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.dto.SupplierResponse;
import com.erp.erp.model.Supplier;
import com.erp.erp.service.SupplierService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/suppliers")
@Tag(name = "Supplier", description = "Manage company Supplier data")

public class SupplierController {

        private final SupplierService supplierService;

        public SupplierController(SupplierService supplierService) {
                this.supplierService = supplierService;
        }

        // Mapping Supplier entity ke SupplierResponse DTO
        private SupplierResponse mapToResponse(Supplier supplier) {
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

        // GET all suppliers with pagination
        @GetMapping
        public ResponseEntity<ApiResponseDto<Page<SupplierResponse>>> getAllSuppliers(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {
                Pageable pageable = PageRequest.of(page, size);
                Page<SupplierResponse> suppliers = supplierService.getAllSuppliers(pageable)
                                .map(this::mapToResponse);

                return ResponseEntity.ok(
                                ApiResponseDto.<Page<SupplierResponse>>builder()
                                                .status("success")
                                                .message("Suppliers fetched successfully")
                                                .data(suppliers)
                                                .build());
        }

        // GET supplier by ID
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponseDto<SupplierResponse>> getSupplierById(@PathVariable Long id) {
                return supplierService.getSupplierById(id)
                                .map(supplier -> ResponseEntity.ok(
                                                ApiResponseDto.<SupplierResponse>builder()
                                                                .status("success")
                                                                .message("Supplier fetched successfully")
                                                                .data(mapToResponse(supplier))
                                                                .build()))
                                .orElse(ResponseEntity.status(404).body(
                                                ApiResponseDto.<SupplierResponse>builder()
                                                                .status("error")
                                                                .message("Supplier not found")
                                                                .data(null)
                                                                .build()));
        }

        // POST create new supplier
        @PostMapping
        public ResponseEntity<ApiResponseDto<SupplierResponse>> createSupplier(@RequestBody Supplier supplier) {
                Supplier created = supplierService.createSupplier(supplier);
                return ResponseEntity.ok(
                                ApiResponseDto.<SupplierResponse>builder()
                                                .status("success")
                                                .message("Supplier created successfully")
                                                .data(mapToResponse(created))
                                                .build());
        }

        // PUT update supplier
        @PutMapping("/{id}")
        public ResponseEntity<ApiResponseDto<SupplierResponse>> updateSupplier(
                        @PathVariable Long id,
                        @RequestBody Supplier supplier) {
                Supplier updated = supplierService.updateSupplier(id, supplier);
                return ResponseEntity.ok(
                                ApiResponseDto.<SupplierResponse>builder()
                                                .status("success")
                                                .message("Supplier updated successfully")
                                                .data(mapToResponse(updated))
                                                .build());
        }

}

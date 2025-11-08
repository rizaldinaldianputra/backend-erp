package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.dto.SupplierResponse;
import com.erp.erp.model.Supplier;
import com.erp.erp.service.SupplierService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/suppliers")
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

        // GET all suppliers
        @GetMapping
        public ResponseEntity<ApiResponseDto<List<SupplierResponse>>> getAllSuppliers() {
                List<SupplierResponse> suppliers = supplierService.getAllSuppliers()
                                .stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());

                return ResponseEntity.ok(
                                ApiResponseDto.<List<SupplierResponse>>builder()
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

        // DELETE supplier
        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponseDto<Void>> deleteSupplier(@PathVariable Long id) {
                supplierService.deleteSupplier(id);
                return ResponseEntity.ok(
                                ApiResponseDto.<Void>builder()
                                                .status("success")
                                                .message("Supplier deleted successfully")
                                                .data(null)
                                                .build());
        }
}

package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.model.Product;
import com.erp.erp.service.ProductService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product", description = "Manage company Product data")

public class ProductController {

        private final ProductService productService;

        public ProductController(ProductService productService) {
                this.productService = productService;
        }

        // GET all products with pagination
        @GetMapping
        @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'SUPERADMIN', 'MANAGER', 'SALES', 'PURCHASING', 'WAREHOUSE', 'STAFF')")
        public ResponseEntity<ApiResponseDto<Page<Product>>> getAllProducts(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {
                Pageable pageable = PageRequest.of(page, size);
                Page<Product> products = productService.getAllProducts(pageable);
                return ResponseEntity.ok(
                                ApiResponseDto.<Page<Product>>builder()
                                                .status("success")
                                                .message("Products fetched successfully")
                                                .data(products)
                                                .build());
        }

        // GET product by ID
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponseDto<Product>> getProductById(@PathVariable Long id) {
                return productService.getProductById(id)
                                .map(product -> ResponseEntity.ok(
                                                ApiResponseDto.<Product>builder()
                                                                .status("success")
                                                                .message("Product fetched successfully")
                                                                .data(product)
                                                                .build()))
                                .orElse(ResponseEntity.status(404).body(
                                                ApiResponseDto.<Product>builder()
                                                                .status("error")
                                                                .message("Product not found")
                                                                .data(null)
                                                                .build()));
        }

        // POST create new product
        @PostMapping
        @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'SUPERADMIN', 'MANAGER', 'PURCHASING', 'STAFF')")
        public ResponseEntity<ApiResponseDto<Product>> createProduct(@RequestBody Product product) {
                Product saved = productService.createProduct(product);
                return ResponseEntity.ok(
                                ApiResponseDto.<Product>builder()
                                                .status("success")
                                                .message("Product created successfully")
                                                .data(saved)
                                                .build());
        }

        // PUT update product
        @PutMapping("/{id}")
        @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'SUPERADMIN', 'MANAGER', 'PURCHASING', 'STAFF')")
        public ResponseEntity<ApiResponseDto<Product>> updateProduct(
                        @PathVariable Long id,
                        @RequestBody Product product) {
                Product updated = productService.updateProduct(id, product);
                return ResponseEntity.ok(
                                ApiResponseDto.<Product>builder()
                                                .status("success")
                                                .message("Product updated successfully")
                                                .data(updated)
                                                .build());
        }

        // DELETE product by ID
        @DeleteMapping("/{id}")
        @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'SUPERADMIN', 'MANAGER', 'PURCHASING')")

        public ResponseEntity<ApiResponseDto<Void>> deleteProduct(@PathVariable Long id) {
                try {
                        productService.deleteProduct(id);
                        return ResponseEntity.ok(
                                        ApiResponseDto.<Void>builder()
                                                        .status("success")
                                                        .message("Product deleted successfully")
                                                        .data(null)
                                                        .build());
                } catch (Exception e) {
                        return ResponseEntity.status(500).body(
                                        ApiResponseDto.<Void>builder()
                                                        .status("error")
                                                        .message("Failed to delete product: " + e.getMessage())
                                                        .data(null)
                                                        .build());
                }
        }

}

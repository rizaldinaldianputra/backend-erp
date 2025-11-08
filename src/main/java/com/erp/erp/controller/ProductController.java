package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.model.Product;
import com.erp.erp.service.ProductService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

        private final ProductService productService;

        public ProductController(ProductService productService) {
                this.productService = productService;
        }

        // GET all products
        @GetMapping
        public ResponseEntity<ApiResponseDto<List<Product>>> getAllProducts() {
                List<Product> products = productService.getAllProducts();
                return ResponseEntity.ok(
                                ApiResponseDto.<List<Product>>builder()
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

        // DELETE product
        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponseDto<Void>> deleteProduct(@PathVariable Long id) {
                productService.deleteProduct(id);
                return ResponseEntity.ok(
                                ApiResponseDto.<Void>builder()
                                                .status("success")
                                                .message("Product deleted successfully")
                                                .data(null)
                                                .build());
        }
}

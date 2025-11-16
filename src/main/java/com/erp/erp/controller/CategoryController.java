package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.model.Category;
import com.erp.erp.service.CategoryService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Categories", description = "Product Categories")

public class CategoryController {

        private final CategoryService categoryService;

        public CategoryController(CategoryService categoryService) {
                this.categoryService = categoryService;
        }

        @GetMapping
        public ResponseEntity<ApiResponseDto<Page<Category>>> getAllCategories(
                        @PageableDefault(size = 10) Pageable pageable) {
                Page<Category> categoriesPage = categoryService.getAllCategories(pageable);
                return ResponseEntity.ok(
                                ApiResponseDto.<Page<Category>>builder()
                                                .status("success")
                                                .message("Categories fetched successfully")
                                                .data(categoriesPage)
                                                .build());
        }

        // GET category by ID
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponseDto<Category>> getCategoryById(@PathVariable Long id) {
                return categoryService.getCategoryById(id)
                                .map(category -> ResponseEntity.ok(
                                                ApiResponseDto.<Category>builder()
                                                                .status("success")
                                                                .message("Category fetched successfully")
                                                                .data(category)
                                                                .build()))
                                .orElse(ResponseEntity.status(404).body(
                                                ApiResponseDto.<Category>builder()
                                                                .status("error")
                                                                .message("Category not found")
                                                                .data(null)
                                                                .build()));
        }

        // POST create new category
        @PostMapping
        public ResponseEntity<ApiResponseDto<Category>> createCategory(@RequestBody Category category) {
                Category saved = categoryService.createCategory(category);
                return ResponseEntity.ok(
                                ApiResponseDto.<Category>builder()
                                                .status("success")
                                                .message("Category created successfully")
                                                .data(saved)
                                                .build());
        }

        // PUT update category
        @PutMapping("/{id}")
        public ResponseEntity<ApiResponseDto<Category>> updateCategory(
                        @PathVariable Long id,
                        @RequestBody Category category) {
                Category updated = categoryService.updateCategory(id, category);
                return ResponseEntity.ok(
                                ApiResponseDto.<Category>builder()
                                                .status("success")
                                                .message("Category updated successfully")
                                                .data(updated)
                                                .build());
        }

}

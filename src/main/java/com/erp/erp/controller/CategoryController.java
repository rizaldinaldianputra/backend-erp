package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponse;
import com.erp.erp.model.Category;
import com.erp.erp.service.category.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // GET all categories
    @GetMapping
    public ResponseEntity<ApiResponse<List<Category>>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(
                ApiResponse.<List<Category>>builder()
                        .status("success")
                        .message("Categories fetched successfully")
                        .data(categories)
                        .build()
        );
    }

    // GET category by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(category -> ResponseEntity.ok(
                        ApiResponse.<Category>builder()
                                .status("success")
                                .message("Category fetched successfully")
                                .data(category)
                                .build()
                ))
                .orElse(ResponseEntity.status(404).body(
                        ApiResponse.<Category>builder()
                                .status("error")
                                .message("Category not found")
                                .data(null)
                                .build()
                ));
    }

    // POST create new category
    @PostMapping
    public ResponseEntity<ApiResponse<Category>> createCategory(@RequestBody Category category) {
        Category saved = categoryService.createCategory(category);
        return ResponseEntity.ok(
                ApiResponse.<Category>builder()
                        .status("success")
                        .message("Category created successfully")
                        .data(saved)
                        .build()
        );
    }

    // PUT update category
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> updateCategory(
            @PathVariable Long id,
            @RequestBody Category category
    ) {
        Category updated = categoryService.updateCategory(id, category);
        return ResponseEntity.ok(
                ApiResponse.<Category>builder()
                        .status("success")
                        .message("Category updated successfully")
                        .data(updated)
                        .build()
        );
    }

    // DELETE category
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .status("success")
                        .message("Category deleted successfully")
                        .data(null)
                        .build()
        );
    }
}

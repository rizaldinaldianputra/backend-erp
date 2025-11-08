package com.erp.erp.service;

import com.erp.erp.model.Category;
import com.erp.erp.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @SuppressWarnings("null")
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    @SuppressWarnings("null")
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @SuppressWarnings("null")
    public Category updateCategory(Long id, Category category) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        existing.setName(category.getName());
        existing.setDescription(category.getDescription());
        existing.setImageUrl(category.getImageUrl());
        existing.setActive(category.getActive());

        return categoryRepository.save(existing);
    }

    @SuppressWarnings("null")
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}

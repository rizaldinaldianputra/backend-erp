package com.erp.erp.repository;

import com.erp.erp.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Cari kategori berdasarkan nama
    Optional<Category> findByName(String name);
}

package com.erp.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erp.erp.model.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    // Contoh custom query kalau dibutuhkan
    boolean existsByName(String name);
}

package com.erp.erp.repository;

import com.erp.erp.model.StockOpname;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockOpnameRepository extends JpaRepository<StockOpname, Long> {
    Optional<StockOpname> findByCode(String code);
}

package com.erp.erp.repository;

import com.erp.erp.model.PurchaseReturn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchaseReturnRepository extends JpaRepository<PurchaseReturn, Long> {
    Optional<PurchaseReturn> findByCode(String code);
}

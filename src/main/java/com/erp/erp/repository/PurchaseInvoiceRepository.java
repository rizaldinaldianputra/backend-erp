package com.erp.erp.repository;

import com.erp.erp.model.PurchaseInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchaseInvoiceRepository extends JpaRepository<PurchaseInvoice, Long> {
    Optional<PurchaseInvoice> findByCode(String code);
}

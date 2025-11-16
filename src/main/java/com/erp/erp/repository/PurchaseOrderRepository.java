package com.erp.erp.repository;

import com.erp.erp.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    Optional<PurchaseOrder> findByDocumentNumber(String documentNumber);

    boolean existsByDocumentNumber(String documentNumber);
}

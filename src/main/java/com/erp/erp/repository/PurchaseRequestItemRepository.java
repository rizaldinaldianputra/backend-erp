package com.erp.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.erp.erp.model.PurchaseRequestItem;

public interface PurchaseRequestItemRepository extends JpaRepository<PurchaseRequestItem, Long> {
}

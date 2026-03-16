package com.erp.erp.repository;

import com.erp.erp.model.StockAdjustmentItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockAdjustmentItemRepository extends JpaRepository<StockAdjustmentItem, Long> {
    List<StockAdjustmentItem> findByStockAdjustmentId(Long stockAdjustmentId);
}

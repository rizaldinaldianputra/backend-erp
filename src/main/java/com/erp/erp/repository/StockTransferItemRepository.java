package com.erp.erp.repository;

import com.erp.erp.model.StockTransferItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockTransferItemRepository extends JpaRepository<StockTransferItem, Long> {
    List<StockTransferItem> findByStockTransferId(Long stockTransferId);
}

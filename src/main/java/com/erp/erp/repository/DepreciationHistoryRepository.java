package com.erp.erp.repository;

import com.erp.erp.model.DepreciationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepreciationHistoryRepository extends JpaRepository<DepreciationHistory, Long> {
    List<DepreciationHistory> findByFixedAssetId(Long fixedAssetId);
}

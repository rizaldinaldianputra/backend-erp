package com.erp.erp.repository;

import com.erp.erp.model.FixedAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FixedAssetRepository extends JpaRepository<FixedAsset, Long> {
    Optional<FixedAsset> findByCode(String code);
}

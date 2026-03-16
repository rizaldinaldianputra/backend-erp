package com.erp.erp.repository;

import com.erp.erp.model.Bom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BomRepository extends JpaRepository<Bom, Long> {
    Optional<Bom> findByCode(String code);
}

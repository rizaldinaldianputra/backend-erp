package com.erp.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erp.erp.model.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

    boolean existsByTitle(String title);
}

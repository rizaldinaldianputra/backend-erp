package com.erp.erp.repository;

import com.erp.erp.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    List<Warehouse> findByOrganizationId(Long organizationId);
}

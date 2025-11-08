package com.erp.erp.repository;

import com.erp.erp.model.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfficeRepository extends JpaRepository<Office, Long> {

    // Cari semua office berdasarkan organization ID
    List<Office> findByOrganizationId(Long organizationId);
}

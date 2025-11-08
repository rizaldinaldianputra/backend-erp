package com.erp.erp.repository;

import com.erp.erp.model.MasterService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterServiceRepository extends JpaRepository<MasterService, Long> {
}

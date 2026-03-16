package com.erp.erp.repository;

import com.erp.erp.model.ApprovalConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApprovalConfigurationRepository extends JpaRepository<ApprovalConfiguration, Long> {
    List<ApprovalConfiguration> findByDocumentTypeAndActiveTrueOrderByMinAmountAsc(String documentType);
}

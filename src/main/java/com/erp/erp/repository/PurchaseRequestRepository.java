package com.erp.erp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.erp.erp.model.PurchaseRequest;

public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, Long> {
    Optional<PurchaseRequest> findByDocumentNumber(String documentNumber);

}

// src/main/java/com/erp/erp/repository/PurchaseRequestRepository.java
package com.erp.erp.repository;

import com.erp.erp.model.PurchaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, Long> {
    Optional<PurchaseRequest> findByDocumentNumber(String documentNumber);
}

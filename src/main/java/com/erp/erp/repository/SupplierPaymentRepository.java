package com.erp.erp.repository;

import com.erp.erp.model.SupplierPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierPaymentRepository extends JpaRepository<SupplierPayment, Long> {
    Optional<SupplierPayment> findByCode(String code);
}

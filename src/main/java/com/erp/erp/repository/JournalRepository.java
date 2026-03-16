package com.erp.erp.repository;

import com.erp.erp.model.JournalHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JournalRepository extends JpaRepository<JournalHeader, Long> {
    Optional<JournalHeader> findByDocumentNumber(String documentNumber);
}

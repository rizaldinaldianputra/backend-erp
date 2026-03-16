package com.erp.erp.repository;

import com.erp.erp.dto.AccountBalanceDTO;
import com.erp.erp.model.JournalDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JournalDetailRepository extends JpaRepository<JournalDetail, Long> {

    @Query("SELECT new com.erp.erp.dto.AccountBalanceDTO(c.code, c.name, SUM(d.debit), SUM(d.credit), SUM(d.debit) - SUM(d.credit)) "
            +
            "FROM JournalDetail d JOIN d.chartOfAccount c JOIN d.header h " +
            "WHERE h.transactionDate BETWEEN :startDate AND :endDate AND h.status = 'POSTED' " +
            "GROUP BY c.code, c.name " +
            "ORDER BY c.code")
    List<AccountBalanceDTO> getAccountBalances(@Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT new com.erp.erp.dto.AccountBalanceDTO(c.code, c.name, SUM(d.debit), SUM(d.credit), SUM(d.debit) - SUM(d.credit)) "
            +
            "FROM JournalDetail d JOIN d.chartOfAccount c JOIN d.header h " +
            "WHERE h.transactionDate BETWEEN :startDate AND :endDate AND h.status = 'POSTED' AND c.type = :accountType "
            +
            "GROUP BY c.code, c.name " +
            "ORDER BY c.code")
    List<AccountBalanceDTO> getAccountBalancesByType(@Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("accountType") com.erp.erp.model.ChartOfAccount.AccountType accountType);
}

package com.erp.erp.service;

import com.erp.erp.dto.AccountBalanceDTO;
import com.erp.erp.dto.FinancialReportDTO;
import com.erp.erp.model.ChartOfAccount.AccountType;
import com.erp.erp.repository.JournalDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FinancialReportService {

    private final JournalDetailRepository journalDetailRepository;

    public FinancialReportDTO getTrialBalance(LocalDate startDate, LocalDate endDate) {
        List<AccountBalanceDTO> balances = journalDetailRepository.getAccountBalances(startDate, endDate);
        return FinancialReportDTO.builder()
                .startDate(startDate)
                .endDate(endDate)
                .trialBalance(balances)
                .build();
    }

    public FinancialReportDTO getBalanceSheet(LocalDate startDate, LocalDate endDate) {
        List<AccountBalanceDTO> assets = journalDetailRepository.getAccountBalancesByType(startDate, endDate,
                AccountType.ASSET);
        List<AccountBalanceDTO> liabilities = journalDetailRepository.getAccountBalancesByType(startDate, endDate,
                AccountType.LIABILITY);
        List<AccountBalanceDTO> equity = journalDetailRepository.getAccountBalancesByType(startDate, endDate,
                AccountType.EQUITY);

        Map<String, List<AccountBalanceDTO>> grouped = new HashMap<>();
        grouped.put("ASSET", assets);
        grouped.put("LIABILITY", liabilities);
        grouped.put("EQUITY", equity);

        // Calculate totals. For Asset, normal balance is Debit. For Liability/Equity,
        // normal balance is Credit.
        BigDecimal totalAssets = assets.stream().map(AccountBalanceDTO::getBalance).reduce(BigDecimal.ZERO,
                BigDecimal::add);
        BigDecimal totalLiabilities = liabilities.stream().map(a -> a.getCredit().subtract(a.getDebit()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalEquity = equity.stream().map(a -> a.getCredit().subtract(a.getDebit())).reduce(BigDecimal.ZERO,
                BigDecimal::add);

        return FinancialReportDTO.builder()
                .startDate(startDate)
                .endDate(endDate)
                .groupedBalances(grouped)
                .totalAssets(totalAssets)
                .totalLiabilities(totalLiabilities)
                .totalEquity(totalEquity)
                .build();
    }

    public FinancialReportDTO getIncomeStatement(LocalDate startDate, LocalDate endDate) {
        List<AccountBalanceDTO> revenues = journalDetailRepository.getAccountBalancesByType(startDate, endDate,
                AccountType.REVENUE);
        List<AccountBalanceDTO> expenses = journalDetailRepository.getAccountBalancesByType(startDate, endDate,
                AccountType.EXPENSE);

        Map<String, List<AccountBalanceDTO>> grouped = new HashMap<>();
        grouped.put("REVENUE", revenues);
        grouped.put("EXPENSE", expenses);

        // Revenue normal is Credit, Expense normal is Debit
        BigDecimal totalRevenue = revenues.stream().map(a -> a.getCredit().subtract(a.getDebit()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalExpense = expenses.stream().map(AccountBalanceDTO::getBalance).reduce(BigDecimal.ZERO,
                BigDecimal::add);
        BigDecimal netIncome = totalRevenue.subtract(totalExpense);

        return FinancialReportDTO.builder()
                .startDate(startDate)
                .endDate(endDate)
                .groupedBalances(grouped)
                .totalRevenue(totalRevenue)
                .totalExpense(totalExpense)
                .netIncome(netIncome)
                .build();
    }
}

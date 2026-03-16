package com.erp.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialReportDTO {
    private LocalDate startDate;
    private LocalDate endDate;

    // For Trial Balance
    private List<AccountBalanceDTO> trialBalance;

    // For Balance Sheet & Income Statement, grouping by Account Type
    private Map<String, List<AccountBalanceDTO>> groupedBalances;

    private BigDecimal totalAssets;
    private BigDecimal totalLiabilities;
    private BigDecimal totalEquity;

    private BigDecimal totalRevenue;
    private BigDecimal totalExpense;
    private BigDecimal netIncome;
}

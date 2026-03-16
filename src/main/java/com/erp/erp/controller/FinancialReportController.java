package com.erp.erp.controller;

import com.erp.erp.dto.FinancialReportDTO;
import com.erp.erp.service.FinancialReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/finance/reports")
@RequiredArgsConstructor
public class FinancialReportController {

    private final FinancialReportService financialReportService;

    @GetMapping("/trial-balance")
    public ResponseEntity<FinancialReportDTO> getTrialBalance(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(financialReportService.getTrialBalance(startDate, endDate));
    }

    @GetMapping("/balance-sheet")
    public ResponseEntity<FinancialReportDTO> getBalanceSheet(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(financialReportService.getBalanceSheet(startDate, endDate));
    }

    @GetMapping("/income-statement")
    public ResponseEntity<FinancialReportDTO> getIncomeStatement(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(financialReportService.getIncomeStatement(startDate, endDate));
    }
}

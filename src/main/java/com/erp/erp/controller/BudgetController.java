package com.erp.erp.controller;

import com.erp.erp.dto.finance.BudgetRequest;
import com.erp.erp.dto.finance.BudgetResponse;
import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<ApiResponseDto<BudgetResponse>> createBudget(@RequestBody BudgetRequest request) {
        BudgetResponse response = budgetService.createBudget(request);
        return ResponseEntity.ok(ApiResponseDto.<BudgetResponse>builder()
                .status("success")
                .message("Budget created successfully")
                .data(response)
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<BudgetResponse>>> getAllBudgets() {
        return ResponseEntity.ok(ApiResponseDto.<List<BudgetResponse>>builder()
                .status("success")
                .message("Success")
                .data(budgetService.getAllBudgets())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<BudgetResponse>> getBudgetById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDto.<BudgetResponse>builder()
                .status("success")
                .message("Success")
                .data(budgetService.getBudgetById(id))
                .build());
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<ApiResponseDto<BudgetResponse>> approveBudget(@PathVariable Long id) {
        BudgetResponse response = budgetService.approveBudget(id);
        return ResponseEntity.ok(ApiResponseDto.<BudgetResponse>builder()
                .status("success")
                .message("Budget approved successfully")
                .data(response)
                .build());
    }
}

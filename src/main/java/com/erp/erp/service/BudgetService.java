package com.erp.erp.service;

import com.erp.erp.dto.finance.BudgetRequest;
import com.erp.erp.dto.finance.BudgetResponse;
import com.erp.erp.model.Budget;
import com.erp.erp.model.BudgetItem;
import com.erp.erp.model.ChartOfAccount;
import com.erp.erp.model.Department;
import com.erp.erp.repository.BudgetRepository;
import com.erp.erp.repository.ChartOfAccountRepository;
import com.erp.erp.repository.DepartmentRepository;
import com.erp.erp.util.CodeGeneratorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final DepartmentRepository departmentRepository;
    private final ChartOfAccountRepository chartOfAccountRepository;
    private final CodeGeneratorUtil codeGeneratorUtil;

    @Transactional
    public BudgetResponse createBudget(BudgetRequest request) {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        String code = codeGeneratorUtil.generateCode("BUDGET");

        Budget budget = Budget.builder()
                .code(code)
                .name(request.getName())
                .fiscalYear(request.getFiscalYear())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .department(department)
                .status(Budget.Status.DRAFT)
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal[] totalAmount = { BigDecimal.ZERO };

        List<BudgetItem> items = request.getItems().stream().map(itemRequest -> {
            ChartOfAccount coa = chartOfAccountRepository.findById(itemRequest.getChartOfAccountId())
                    .orElseThrow(() -> new RuntimeException("Chart of Account not found"));

            totalAmount[0] = totalAmount[0].add(itemRequest.getAllocatedAmount());

            return BudgetItem.builder()
                    .budget(budget)
                    .chartOfAccount(coa)
                    .allocatedAmount(itemRequest.getAllocatedAmount())
                    .utilizedAmount(BigDecimal.ZERO)
                    .note(itemRequest.getNote())
                    .build();
        }).collect(Collectors.toList());

        budget.setItems(items);
        budget.setTotalAmount(totalAmount[0]);

        Budget savedBudget = budgetRepository.save(budget);
        return mapToResponse(savedBudget);
    }

    @Transactional(readOnly = true)
    public List<BudgetResponse> getAllBudgets() {
        return budgetRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BudgetResponse getBudgetById(Long id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));
        return mapToResponse(budget);
    }

    @Transactional
    public BudgetResponse approveBudget(Long id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));
        budget.setStatus(Budget.Status.APPROVED);
        Budget updatedBudget = budgetRepository.save(budget);
        return mapToResponse(updatedBudget);
    }

    private BudgetResponse mapToResponse(Budget budget) {
        return BudgetResponse.builder()
                .id(budget.getId())
                .code(budget.getCode())
                .name(budget.getName())
                .fiscalYear(budget.getFiscalYear())
                .startDate(budget.getStartDate())
                .endDate(budget.getEndDate())
                .departmentId(budget.getDepartment().getId())
                .departmentName(budget.getDepartment().getName())
                .totalAmount(budget.getTotalAmount())
                .status(budget.getStatus().name())
                .items(budget.getItems().stream()
                        .map(item -> BudgetResponse.BudgetItemResponse.builder()
                                .id(item.getId())
                                .chartOfAccountId(item.getChartOfAccount().getId())
                                .chartOfAccountCode(item.getChartOfAccount().getCode())
                                .chartOfAccountName(item.getChartOfAccount().getName())
                                .allocatedAmount(item.getAllocatedAmount())
                                .utilizedAmount(item.getUtilizedAmount())
                                .note(item.getNote())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}

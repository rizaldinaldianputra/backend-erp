package com.erp.erp.dto.finance;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class BudgetRequest {
    private String name;
    private Integer fiscalYear;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long departmentId;
    private List<BudgetItemRequest> items;

    @Data
    public static class BudgetItemRequest {
        private Long chartOfAccountId;
        private BigDecimal allocatedAmount;
        private String note;
    }
}

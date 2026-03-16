package com.erp.erp.dto.finance;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class BudgetResponse {
    private Long id;
    private String code;
    private String name;
    private Integer fiscalYear;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long departmentId;
    private String departmentName;
    private BigDecimal totalAmount;
    private String status;
    private List<BudgetItemResponse> items;

    @Data
    @Builder
    public static class BudgetItemResponse {
        private Long id;
        private Long chartOfAccountId;
        private String chartOfAccountCode;
        private String chartOfAccountName;
        private BigDecimal allocatedAmount;
        private BigDecimal utilizedAmount;
        private String note;
    }
}

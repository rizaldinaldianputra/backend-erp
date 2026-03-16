package com.erp.erp.dto.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {

    private Long id;
    private String code;
    private String name;
    private String description;
    private Long customerId;
    private String customerName;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal budget;
    private String status;
    private List<ProjectTaskResponse> tasks;
    private List<ProjectBudgetResponse> budgets;
    private LocalDateTime createdDate;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectTaskResponse {
        private Long id;
        private String name;
        private String description;
        private Long assigneeId;
        private String assigneeName;
        private LocalDate startDate;
        private LocalDate endDate;
        private String status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectBudgetResponse {
        private Long id;
        private String category;
        private BigDecimal plannedAmount;
        private BigDecimal actualAmount;
        private String note;
    }
}

package com.erp.erp.dto.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequest {

    private String name;
    private String description;
    private Long customerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal budget;
    private List<ProjectTaskRequest> tasks;
    private List<ProjectBudgetRequest> budgets;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectTaskRequest {
        private String name;
        private String description;
        private Long assigneeId;
        private LocalDate startDate;
        private LocalDate endDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectBudgetRequest {
        private String category;
        private BigDecimal plannedAmount;
        private String note;
    }
}

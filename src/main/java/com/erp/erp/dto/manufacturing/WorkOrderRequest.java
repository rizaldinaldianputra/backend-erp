package com.erp.erp.dto.manufacturing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrderRequest {

    private Long bomId;
    private Long productId;
    private Long warehouseId;
    private Integer plannedQuantity;
    private LocalDate startDate;
    private LocalDate endDate;
    private String note;
    private List<WorkOrderItemRequest> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorkOrderItemRequest {
        private Long materialId;
        private Integer plannedQuantity;
        private String note;
    }
}

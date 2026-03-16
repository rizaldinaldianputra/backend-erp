package com.erp.erp.dto.manufacturing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrderResponse {

    private Long id;
    private String code;
    private Long bomId;
    private String bomName;
    private Long productId;
    private String productName;
    private Long warehouseId;
    private String warehouseName;
    private Integer plannedQuantity;
    private Integer producedQuantity;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String note;
    private List<WorkOrderItemResponse> items;
    private LocalDateTime createdDate;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorkOrderItemResponse {
        private Long id;
        private Long materialId;
        private String materialName;
        private Integer plannedQuantity;
        private Integer consumedQuantity;
        private String note;
    }
}

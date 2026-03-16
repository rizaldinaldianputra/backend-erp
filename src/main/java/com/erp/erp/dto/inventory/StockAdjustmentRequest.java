package com.erp.erp.dto.inventory;

import lombok.Data;

@Data
public class StockAdjustmentRequest {
    private Long productId;
    private Long warehouseId;
    private Integer actualQuantity; // For Opname (count)
    private Integer adjustmentQuantity; // For Adjustment (+/-)
    private String type; // "OPNAME" or "ADJUSTMENT"
    private String description;
}

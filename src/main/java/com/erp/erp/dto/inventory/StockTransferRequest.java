package com.erp.erp.dto.inventory;

import lombok.Data;

@Data
public class StockTransferRequest {
    private Long productId;
    private Long fromWarehouseId;
    private Long toWarehouseId;
    private Integer quantity;
    private String description;
}

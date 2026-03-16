package com.erp.erp.dto.purchase;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class GoodsReceiptRequest {
    private Long purchaseOrderId;
    private Long warehouseId;
    private LocalDate date;
    private String note;
    private List<GoodsReceiptItemRequest> items;

    @Data
    public static class GoodsReceiptItemRequest {
        private Long productId;
        private Integer quantity;
    }
}

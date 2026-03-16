package com.erp.erp.dto.purchase;

import com.erp.erp.model.PurchaseReturnItem;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PurchaseReturnRequest {
    private Long purchaseOrderId;
    private Long goodsReceiptId;
    private Long supplierId;
    private Long warehouseId;
    private LocalDate returnDate;
    private String note;
    private List<PurchaseReturnItemRequest> items;

    @Data
    public static class PurchaseReturnItemRequest {
        private Long productId;
        private Integer quantity;
        private PurchaseReturnItem.Condition condition;
        private String reason;
    }
}

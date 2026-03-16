package com.erp.erp.dto.purchase;

import com.erp.erp.model.PurchaseReturnItem;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class PurchaseReturnResponse {
    private Long id;
    private String code;
    private Long purchaseOrderId;
    private String purchaseOrderCode;
    private Long goodsReceiptId;
    private String goodsReceiptCode;
    private Long supplierId;
    private String supplierName;
    private Long warehouseId;
    private String warehouseName;
    private LocalDate returnDate;
    private String note;
    private BigDecimal totalAmount;
    private String status;
    private List<PurchaseReturnItemResponse> items;

    @Data
    @Builder
    public static class PurchaseReturnItemResponse {
        private Long id;
        private Long productId;
        private String productName;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal subTotal;
        private String reason;
        private PurchaseReturnItem.Condition condition;
    }
}

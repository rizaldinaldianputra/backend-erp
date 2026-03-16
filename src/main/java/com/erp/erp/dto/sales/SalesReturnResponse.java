package com.erp.erp.dto.sales;

import com.erp.erp.model.SalesReturnItem;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class SalesReturnResponse {
    private Long id;
    private String code;
    private Long salesOrderId;
    private String salesOrderCode;
    private Long deliveryOrderId;
    private String deliveryOrderCode;
    private Long customerId;
    private String customerName;
    private Long warehouseId;
    private String warehouseName;
    private LocalDate returnDate;
    private String note;
    private BigDecimal totalAmount;
    private String status;
    private List<SalesReturnItemResponse> items;

    @Data
    @Builder
    public static class SalesReturnItemResponse {
        private Long id;
        private Long productId;
        private String productName;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal subTotal;
        private String reason;
        private SalesReturnItem.Condition condition;
    }
}

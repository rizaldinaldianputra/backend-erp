package com.erp.erp.dto.sales;

import com.erp.erp.model.SalesReturnItem;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SalesReturnRequest {
    private Long salesOrderId;
    private Long deliveryOrderId;
    private Long customerId;
    private Long warehouseId;
    private LocalDate returnDate;
    private String note;
    private List<SalesReturnItemRequest> items;

    @Data
    public static class SalesReturnItemRequest {
        private Long productId;
        private Integer quantity;
        private SalesReturnItem.Condition condition;
        private String reason;
    }
}

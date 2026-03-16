package com.erp.erp.dto.sales;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class DeliveryOrderRequest {
    private Long salesOrderId;
    private Long customerId;
    private Long warehouseId;
    private LocalDate deliveryDate;
    private String note;
    private List<DeliveryOrderItemRequest> items;

    @Data
    public static class DeliveryOrderItemRequest {
        private Long productId;
        private Integer quantity; // Quantity to deliver
        private String note;
    }
}

package com.erp.erp.dto.sales;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class DeliveryOrderResponse {
    private Long id;
    private String code;
    private Long salesOrderId;
    private String salesOrderCode;
    private Long customerId;
    private String customerName;
    private Long warehouseId;
    private String warehouseName;
    private LocalDate deliveryDate;
    private String note;
    private String status;
    private List<DeliveryOrderItemResponse> items;

    @Data
    @Builder
    public static class DeliveryOrderItemResponse {
        private Long id;
        private Long productId;
        private String productName;
        private Integer quantity;
        private Integer deliveredQuantity;
        private String note;
    }
}

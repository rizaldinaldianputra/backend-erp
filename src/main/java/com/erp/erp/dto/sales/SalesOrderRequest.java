package com.erp.erp.dto.sales;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class SalesOrderRequest {
    private Long quotationId; // Optional, convert from Quotation
    private Long customerId;
    private LocalDate date;
    private LocalDate deliveryDate;
    private String note;
    private List<SalesOrderItemRequest> items;

    @Data
    public static class SalesOrderItemRequest {
        private Long productId;
        private Integer quantity;
        private BigDecimal discount;
    }
}

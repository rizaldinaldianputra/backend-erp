package com.erp.erp.dto.sales;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class QuotationRequest {
    private Long customerId;
    private LocalDate date;
    private LocalDate validUntil;
    private String note;
    private List<QuotationItemRequest> items;

    @Data
    public static class QuotationItemRequest {
        private Long productId;
        private Integer quantity;
        private BigDecimal discount;
    }
}

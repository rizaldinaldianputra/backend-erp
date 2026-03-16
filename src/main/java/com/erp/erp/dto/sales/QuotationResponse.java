package com.erp.erp.dto.sales;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class QuotationResponse {
    private Long id;
    private String code;
    private Long customerId;
    private String customerName;
    private LocalDate date;
    private LocalDate validUntil;
    private String note;
    private String status;
    private BigDecimal subTotal;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private List<QuotationItemResponse> items;

    @Data
    @Builder
    public static class QuotationItemResponse {
        private Long id;
        private Long productId;
        private String productName;
        private Integer quantity;
        private BigDecimal price;
        private BigDecimal discount;
        private BigDecimal total;
    }
}

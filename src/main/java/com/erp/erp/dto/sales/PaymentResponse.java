package com.erp.erp.dto.sales;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class PaymentResponse {
    private Long id;
    private String code;
    private Long invoiceId;
    private String invoiceCode;
    private Long customerId;
    private String customerName;
    private LocalDate date;
    private String paymentMethod;
    private BigDecimal amount;
    private String note;
    private String receiptUrl;
}

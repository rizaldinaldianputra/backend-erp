package com.erp.erp.dto.sales;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PaymentRequest {
    private Long invoiceId;
    private Long bankAccountId;
    private LocalDate date;
    private String paymentMethod;
    private BigDecimal amount;
    private String note;
    private String receiptUrl;
}

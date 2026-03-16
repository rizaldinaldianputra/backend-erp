package com.erp.erp.dto.purchase;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SupplierPaymentRequest {
    private Long purchaseInvoiceId;
    private Long bankAccountId;
    private LocalDate date;
    private String paymentMethod;
    private BigDecimal amount;
    private String note;
}

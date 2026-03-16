package com.erp.erp.dto.purchase;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PurchaseInvoiceRequest {
    private Long purchaseOrderId;
    private String supplierInvoiceNumber; // External Ref
    private LocalDate date;
    private LocalDate dueDate;
}

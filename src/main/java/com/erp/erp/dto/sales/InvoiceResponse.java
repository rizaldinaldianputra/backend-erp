package com.erp.erp.dto.sales;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class InvoiceResponse {
    private Long id;
    private String code;
    private Long salesOrderId;
    private String salesOrderCode;
    private Long customerId;
    private String customerName;
    private LocalDate date;
    private LocalDate dueDate;
    private String status;
    private BigDecimal subTotal;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private BigDecimal remainingAmount;
}

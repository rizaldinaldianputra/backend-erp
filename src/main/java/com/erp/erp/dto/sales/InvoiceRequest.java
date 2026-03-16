package com.erp.erp.dto.sales;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class InvoiceRequest {
    private Long salesOrderId;
    private LocalDate date;
    private LocalDate dueDate;
}

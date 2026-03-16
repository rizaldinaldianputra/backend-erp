package com.erp.erp.dto.system;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ApprovalConfigRequest {
    private String documentType;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private String approverRole;
    private Boolean active;
}

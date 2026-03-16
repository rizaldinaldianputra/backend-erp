package com.erp.erp.dto.system;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class ApprovalConfigResponse {
    private Long id;
    private String documentType;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private String approverRole;
    private Boolean active;
}

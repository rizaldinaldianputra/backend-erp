package com.erp.erp.dto.finance;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class FixedAssetResponse {
    private Long id;
    private String code;
    private String name;
    private String description;
    private Long chartOfAccountId;
    private String chartOfAccountName;
    private LocalDate purchaseDate;
    private BigDecimal purchasePrice;
    private BigDecimal salvageValue;
    private Integer usefulLifeYears;
    private String depreciationMethod;
    private String status;
    private List<DepreciationHistoryResponse> depreciationHistories;

    @Data
    @Builder
    public static class DepreciationHistoryResponse {
        private Long id;
        private LocalDate depreciationDate;
        private BigDecimal amount;
        private BigDecimal accumulatedDepreciation;
        private BigDecimal bookValue;
        private String note;
    }
}

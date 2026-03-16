package com.erp.erp.dto.finance;

import com.erp.erp.model.FixedAsset;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class FixedAssetRequest {
    private String name;
    private String description;
    private Long chartOfAccountId;
    private LocalDate purchaseDate;
    private BigDecimal purchasePrice;
    private BigDecimal salvageValue;
    private Integer usefulLifeYears;
    private FixedAsset.DepreciationMethod depreciationMethod;
}

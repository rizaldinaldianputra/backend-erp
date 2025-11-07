package com.erp.erp.dto.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {
    private Long id;
    private String code;
    private String name;
    private String unit;
    private Double costPrice;
    private String categoryName;  // hanya nama category
    private String description;
    private Boolean active;
    private String barcode;
    private String qrCode;
    private String imageUrl;
}

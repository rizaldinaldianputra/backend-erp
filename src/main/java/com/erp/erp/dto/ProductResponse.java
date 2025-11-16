package com.erp.erp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {
    private Long id;
    private String code;
    private String name;

    private UomResponse uom; // object UOM
    private CategoryResponse category; // object Category

    private Double costPrice;
    private String description;
    private Boolean active;
    private String barcode;
    private String qrCode;
    private String imageUrl;
}

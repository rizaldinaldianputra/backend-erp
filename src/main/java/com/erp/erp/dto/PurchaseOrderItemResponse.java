package com.erp.erp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PurchaseOrderItemResponse {

    private Long id;

    private ProductResponse product; // full object product
    private Integer discount; // Tambahkan discount

    private Integer quantity;
    private Double price;
    private Double subtotal; // price * quantity
}

package com.erp.erp.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseRequestItemResponse {
    private Long id;
    private String type;
    private Long productId;
    private String productName;
    private Long serviceId;
    private String serviceName;
    private Integer quantity;
    private Double estimatedPrice;
    private String reason;
}

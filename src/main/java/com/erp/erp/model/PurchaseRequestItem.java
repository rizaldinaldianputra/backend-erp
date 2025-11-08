package com.erp.erp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "purchase_request_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseRequestItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // PRODUCT / SERVICE

    private Long productId;
    private String productName;

    private Long serviceId;
    private String serviceName;

    private Integer quantity;
    private Double estimatedPrice;
    private String reason;

    @ManyToOne
    @JoinColumn(name = "purchase_request_id")
    private PurchaseRequest purchaseRequest;
}

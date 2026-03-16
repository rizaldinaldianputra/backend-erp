package com.erp.erp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "delivery_order_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_order_id", nullable = false)
    @ToString.Exclude
    private DeliveryOrder deliveryOrder;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Integer quantity; // Quantity to deliver

    private Integer deliveredQuantity; // Actual quantity delivered

    @Column(columnDefinition = "TEXT")
    private String note;
}

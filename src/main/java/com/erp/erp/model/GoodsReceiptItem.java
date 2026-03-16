package com.erp.erp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "goods_receipt_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsReceiptItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_receipt_id", nullable = false)
    @ToString.Exclude
    private GoodsReceipt goodsReceipt;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Integer quantity; // Quantity received
}

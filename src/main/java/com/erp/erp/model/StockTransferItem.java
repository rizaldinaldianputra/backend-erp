package com.erp.erp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stock_transfer_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockTransferItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_transfer_id", nullable = false)
    @ToString.Exclude
    private StockTransfer stockTransfer;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Integer quantity; // Quantity transferred
}

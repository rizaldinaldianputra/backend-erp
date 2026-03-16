package com.erp.erp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stock_opname_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockOpnameItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_opname_id", nullable = false)
    @ToString.Exclude
    private StockOpname stockOpname;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Integer systemQuantity;

    private Integer actualQuantity;

    private Integer difference;

    @Column(columnDefinition = "TEXT")
    private String note;
}

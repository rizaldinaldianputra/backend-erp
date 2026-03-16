package com.erp.erp.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "sales_return_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesReturnItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_return_id", nullable = false)
    @ToString.Exclude
    private SalesReturn salesReturn;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Integer quantity;

    private BigDecimal unitPrice;

    private BigDecimal subTotal;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Condition condition = Condition.GOOD;

    public enum Condition {
        GOOD, DAMAGED, DEFECTIVE
    }
}

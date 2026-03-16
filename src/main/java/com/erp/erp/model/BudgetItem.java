package com.erp.erp.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "budget_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id", nullable = false)
    @ToString.Exclude
    private Budget budget;

    @ManyToOne
    @JoinColumn(name = "chart_of_account_id", nullable = false)
    private ChartOfAccount chartOfAccount;

    private BigDecimal allocatedAmount;

    @Builder.Default
    private BigDecimal utilizedAmount = BigDecimal.ZERO;

    @Column(columnDefinition = "TEXT")
    private String note;
}

package com.erp.erp.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "project_budgets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    @ToString.Exclude
    private Project project;

    @Column(nullable = false)
    private String category; // e.g. MATERIAL, LABOR, OUTSOURCE

    @Builder.Default
    private BigDecimal plannedAmount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal actualAmount = BigDecimal.ZERO;

    @Column(columnDefinition = "TEXT")
    private String note;
}

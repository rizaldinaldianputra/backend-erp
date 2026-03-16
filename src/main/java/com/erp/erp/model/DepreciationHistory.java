package com.erp.erp.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "depreciation_histories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepreciationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixed_asset_id", nullable = false)
    @ToString.Exclude
    private FixedAsset fixedAsset;

    private LocalDate depreciationDate;

    private BigDecimal amount;

    private BigDecimal accumulatedDepreciation;

    private BigDecimal bookValue;

    @Column(columnDefinition = "TEXT")
    private String note;
}

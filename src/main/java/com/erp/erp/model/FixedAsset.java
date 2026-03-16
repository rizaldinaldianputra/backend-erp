package com.erp.erp.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "fixed_assets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class FixedAsset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "chart_of_account_id")
    private ChartOfAccount chartOfAccount;

    private LocalDate purchaseDate;

    private BigDecimal purchasePrice;

    private BigDecimal salvageValue;

    private Integer usefulLifeYears;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private DepreciationMethod depreciationMethod = DepreciationMethod.STRAIGHT_LINE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Status status = Status.ACTIVE;

    @OneToMany(mappedBy = "fixedAsset", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DepreciationHistory> depreciationHistories = new ArrayList<>();

    // Audit Fields
    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedBy
    private String updatedBy;

    @LastModifiedDate
    private LocalDateTime updatedDate;

    public enum DepreciationMethod {
        STRAIGHT_LINE, DOUBLE_DECLINING_BALANCE, UNITS_OF_PRODUCTION
    }

    public enum Status {
        ACTIVE, DISPOSED, SOLD
    }
}

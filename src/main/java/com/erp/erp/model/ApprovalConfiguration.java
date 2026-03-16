package com.erp.erp.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "approval_configurations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ApprovalConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String documentType; // e.g., "PO", "PR", "SALES_ORDER"

    @Column(nullable = false)
    private BigDecimal minAmount;

    @Column(nullable = true)
    private BigDecimal maxAmount; // Null means infinity/no upper limit

    @Column(nullable = true)
    private String approverRole; // "SUPERVISOR", "MANAGER", "DIRECTOR". Null means "Tanpa Approval"

    @Builder.Default
    private Boolean active = true;

    // --- Audit fields ---
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
}

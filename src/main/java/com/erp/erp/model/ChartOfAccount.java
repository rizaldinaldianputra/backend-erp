package com.erp.erp.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Entity
@Table(name = "chart_of_accounts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ChartOfAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Primary key", example = "1")
    private Long id;

    @Schema(description = "Kode Akun", example = "1101")
    @Column(unique = true)
    private String code;

    @Schema(description = "Nama Akun", example = "Kas Besar")
    private String name;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Tipe Akun", example = "ASSET")
    private AccountType type;

    @Schema(description = "Level Akun", example = "1")
    private Integer level;

    @Schema(description = "Parent Account ID (Self Join)", example = "1")
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private ChartOfAccount parent;

    @Schema(description = "Status aktif", example = "true")
    @Builder.Default
    private Boolean active = true;

    // --- Audit Fields ---
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

    public enum AccountType {
        ASSET, LIABILITY, EQUITY, REVENUE, EXPENSE
    }
}

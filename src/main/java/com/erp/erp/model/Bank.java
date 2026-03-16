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
@Table(name = "banks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Bank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Primary key", example = "1")
    private Long id;

    @Schema(description = "Kode Bank", example = "BCA")
    @Column(unique = true)
    private String code;

    @Schema(description = "Nama Bank", example = "Bank Central Asia")
    private String name;

    @Schema(description = "Nomor Rekening", example = "1234567890")
    private String accountNumber;

    @Schema(description = "Cabang", example = "KCU Sudirman")
    private String branch;

    @Schema(description = "Atas Nama", example = "PT Sky ERP")
    private String accountHolder;

    @Schema(description = "URL Logo Bank", example = "http://localhost:9000/skyerp-assets/logo.png")
    private String logoUrl;

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
}

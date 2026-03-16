package com.erp.erp.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Primary key", example = "1")
    private Long id;

    @Schema(description = "Kode customer (otomatis jika kosong)", example = "CUST-001")
    @Column(unique = true)
    private String code;

    @Schema(description = "Nama customer", example = "PT Sejahtera Abadi")
    private String name;

    @Schema(description = "Email customer", example = "purchasing@sejahtera.co.id")
    private String email;

    @Schema(description = "Nomor telepon", example = "021-5559876")
    private String phone;

    @Schema(description = "Alamat lengkap", example = "Jl. Sudirman No. 45, Jakarta")
    @Column(columnDefinition = "TEXT")
    private String address;

    @Schema(description = "Nomor NPWP", example = "12.345.678.9-012.000")
    private String npwp;

    @Schema(description = "Batas kredit", example = "100000000")
    private BigDecimal creditLimit;

    @Schema(description = "Status aktif", example = "true")
    @Builder.Default
    private Boolean active = true;

    // --- Audit Fields ---
    @CreatedBy
    @Column(updatable = false)
    @Schema(example = "admin")
    private String createdBy;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedBy
    @Schema(example = "admin")
    private String updatedBy;

    @LastModifiedDate
    private LocalDateTime updatedDate;

    @PrePersist
    protected void onCreate() {
        if (this.code == null || this.code.isBlank()) {
            this.code = "CUST-" + System.currentTimeMillis();
        }
        if (this.active == null) {
            this.active = true;
        }
    }
}

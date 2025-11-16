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
@Table(name = "organizations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Primary key", example = "1")
    private Long id;

    @Schema(description = "Kode organisasi (otomatis jika kosong)", example = "ORG-17369872345")
    private String code;

    @Schema(description = "Nama organisasi", example = "PT Maju Jaya")
    private String name;

    @Schema(description = "Alamat organisasi", example = "Jl. Melati No. 10, Jakarta")
    private String address;

    @Schema(description = "Nomor telepon", example = "021-5551234")
    private String phone;

    @Schema(description = "Email organisasi", example = "info@majujaya.co.id")
    private String email;

    @Schema(description = "Website", example = "https://www.majujaya.co.id")
    private String website;

    @Schema(description = "URL logo", example = "https://cdn.example.com/logo/majujaya.png")
    private String logoUrl;

    @Schema(description = "Status aktif", example = "true")
    private Boolean active = true;

    // --- Audit Fields ---
    @CreatedBy
    @Column(updatable = false)
    @Schema(example = "system")
    private String createdBy;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedBy
    @Schema(example = "system")
    private String updatedBy;

    @LastModifiedDate
    private LocalDateTime updatedDate;

    // Auto generate code if empty
    @PrePersist
    protected void onCreate() {
        if (this.code == null || this.code.isBlank()) {
            this.code = "ORG-" + System.currentTimeMillis();
        }
    }
}

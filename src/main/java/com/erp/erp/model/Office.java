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
@Table(name = "offices")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Primary Key", example = "1")
    private Long id;

    @Schema(description = "Nama kantor", example = "Kantor Pusat Jakarta")
    private String name;

    @Schema(description = "Alamat kantor", example = "Jl. Sudirman No. 25, Jakarta")
    private String address;

    @Schema(description = "Kode kantor (auto generate jika kosong)", example = "OFF-17369872345")
    private String code;

    @Schema(description = "Nomor telepon", example = "021-88442211")
    private String phone;

    @Schema(description = "Email kantor", example = "office@majujaya.co.id")
    private String email;

    @Schema(description = "Website kantor", example = "https://office.majujaya.co.id")
    private String website;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    @Schema(description = "Relasi ke organisasi")
    private Organization organization;

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

    // Generate code otomatis
    @PrePersist
    protected void onCreate() {
        if (this.code == null || this.code.isBlank()) {
            this.code = "OFF-" + System.currentTimeMillis();
        }
    }
}

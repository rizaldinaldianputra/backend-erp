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
@Table(name = "taxes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Tax {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Primary key", example = "1")
    private Long id;

    @Schema(description = "Kode Pajak", example = "PPN")
    @Column(unique = true)
    private String code;

    @Schema(description = "Nama Pajak", example = "Pajak Pertambahan Nilai")
    private String name;

    @Schema(description = "Tarif Pajak (%)", example = "11.0")
    private BigDecimal rate;

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

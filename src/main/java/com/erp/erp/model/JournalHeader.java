package com.erp.erp.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "journal_headers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class JournalHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Primary key", example = "1")
    private Long id;

    @Schema(description = "Nomor Dokumen", example = "JV-202310-0001")
    @Column(unique = true)
    private String documentNumber;

    @Schema(description = "Tanggal Transaksi", example = "2023-10-25")
    private LocalDate transactionDate;

    @Schema(description = "Keterangan", example = "Jurnal Penyesuaian Oktober")
    private String description;

    @Schema(description = "Nomor Referensi", example = "INV-001")
    private String referenceNumber;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Status Jurnal", example = "POSTED")
    @Builder.Default
    private JournalStatus status = JournalStatus.DRAFT;

    @OneToMany(mappedBy = "header", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JournalDetail> details;

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

    public enum JournalStatus {
        DRAFT, POSTED, CANCELLED
    }
}

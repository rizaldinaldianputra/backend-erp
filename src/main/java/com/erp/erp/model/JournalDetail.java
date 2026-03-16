package com.erp.erp.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Entity
@Table(name = "journal_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JournalDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Primary key", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "header_id")
    @JsonIgnore
    private JournalHeader header;

    @ManyToOne
    @JoinColumn(name = "coa_id")
    private ChartOfAccount chartOfAccount;

    @Schema(description = "Deskripsi Baris", example = "Pembayaran Listrik")
    private String description;

    @Schema(description = "Debit", example = "100000")
    @Builder.Default
    private BigDecimal debit = BigDecimal.ZERO;

    @Schema(description = "Credit", example = "0")
    @Builder.Default
    private BigDecimal credit = BigDecimal.ZERO;
}

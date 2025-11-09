package com.erp.erp.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String code; // kode internal produk

    @Column(nullable = false, length = 100, unique = true)
    private String barcode; // value barcode string

    @Column(name = "qr_code", columnDefinition = "TEXT")
    private String qrCode; // value QR string Base64

    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 50)
    private String unit;

    private BigDecimal costPrice;

    private Boolean active;

    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime updatedDate;
    private String updatedBy;

    @PrePersist
    public void prePersist() {
        if (this.active == null)
            this.active = true;
        if (this.createdDate == null)
            this.createdDate = LocalDateTime.now();
        if (this.barcode == null || this.barcode.isEmpty())
            this.barcode = generateBarcodeValue();
        if (this.qrCode == null || this.qrCode.isEmpty())
            this.qrCode = "DEFAULT_QR"; // bisa diganti service generate QR Base64
    }

    // Generate barcode 12 digit numeric
    private String generateBarcodeValue() {
        long number = (long) (Math.random() * 1_000_000_000_000L);
        return String.format("%012d", number);
    }
}

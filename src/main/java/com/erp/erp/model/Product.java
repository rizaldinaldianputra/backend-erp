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
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String code;

    @Column(nullable = false, length = 100, unique = true)
    private String barcode;

    @Column(name = "qr_code", columnDefinition = "TEXT")
    private String qrCode;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 50)
    private String unit;

    private BigDecimal costPrice;

    private Boolean active;

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

    @PrePersist
    public void prePersist() {
        if (this.active == null)
            this.active = true;
        if (this.code == null || this.code.isBlank())
            this.code = "PRD-" + System.currentTimeMillis();
        if (this.barcode == null || this.barcode.isEmpty())
            this.barcode = generateBarcodeValue();
        if (this.qrCode == null || this.qrCode.isEmpty())
            this.qrCode = "DEFAULT_QR"; // bisa diganti service generate QR Base64
    }

    private String generateBarcodeValue() {
        long number = (long) (Math.random() * 1_000_000_000_000L);
        return String.format("%012d", number);
    }
}

package com.erp.erp.model;

import com.erp.erp.security.SecurityUtil;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    private String unit;

    private Double costPrice;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String description;

    private String barcode;

    private String qrCode;

    private String imageUrl;

    private Boolean active = true;

    // Audit fields
    private String createdBy;
    private LocalDateTime createdDate;
    private String updatedBy;
    private LocalDateTime updatedDate;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
        if (this.createdBy == null) {
            this.createdBy = SecurityUtil.getCurrentUsername();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = LocalDateTime.now();
        this.updatedBy = SecurityUtil.getCurrentUsername();
    }
}

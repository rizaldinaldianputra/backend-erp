package com.erp.erp.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "categories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code; // kode internal, generate otomatis

    @Column(nullable = false, unique = true)
    private String extCode; // kode eksternal bebas

    @Column(nullable = false)
    private String name;

    private String description;

    private String imageUrl;

    private Boolean active = true;

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    private String updatedBy;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // --- PrePersist untuk kode otomatis ---
    @PrePersist
    protected void onCreate() {
        if (this.code == null || this.code.isBlank()) {
            this.code = "CAT-" + System.currentTimeMillis();
        }
    }
}

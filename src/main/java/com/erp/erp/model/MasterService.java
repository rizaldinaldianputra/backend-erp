package com.erp.erp.model;

import com.erp.erp.security.SecurityUtil;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "services")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MasterService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code; // kode jasa unik, misal SVC-20251108-001

    @Column(nullable = false)
    private String name;

    private String description;

    private Double unitPrice;

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

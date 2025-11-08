package com.erp.erp.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.erp.erp.security.SecurityUtil;

@Entity
@Table(name = "warehouses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String code;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

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

package com.erp.erp.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.erp.erp.security.SecurityUtil;

@Entity
@Table(name = "organizations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String website;
    private String logoUrl;
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

package com.erp.erp.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.erp.erp.security.SecurityUtil;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;

    private String role; // ADMIN, USER, MANAGER, dsb

    // Audit fields
    private String createdBy;
    private LocalDateTime createdDate;
    private String updatedBy;
    private LocalDateTime updatedDate;

    @ManyToOne
    @JoinColumn(name = "supervisor_id")
    private User supervisor; // atasan langsung

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

    public String getFullName() {
        return this.username;
    }
}

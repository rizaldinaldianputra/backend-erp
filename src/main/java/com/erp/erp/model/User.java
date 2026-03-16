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
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String fullName;
    private String password;
    private String email;

    private String role; // ADMIN, USER, MANAGER, dsb

    private String avatarUrl;

    // --- Forgot Password OTP ---
    @Column(name = "reset_password_otp")
    private String resetPasswordOtp;

    @Column(name = "reset_password_otp_expiry")
    private LocalDateTime resetPasswordOtpExpiry;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    // --- Audit fields ---
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

    @ManyToOne
    @JoinColumn(name = "supervisor_id")
    private User supervisor; // atasan langsung

    public String getFullName() {
        return this.fullName != null ? this.fullName : this.username;
    }
}

package com.erp.erp.model;

import com.erp.erp.security.SecurityUtil;
import com.erp.erp.workflow.entity.WorkflowEntity;
import com.erp.erp.workflow.rules.PRStatus;
import com.erp.erp.workflow.rules.Status;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "purchase_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseRequest implements WorkflowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String documentNumber;

    private LocalDateTime trxDate;
    private LocalDateTime requiredDate;
    private String notes;

    @ManyToOne
    @JoinColumn(name = "created_by_user_id")
    private User createdBy;

    @Enumerated(EnumType.STRING)
    private PRStatus status;

    @OneToMany(mappedBy = "purchaseRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseRequestItem> items;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
        this.status = PRStatus.DRAFT;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = LocalDateTime.now();
    }

    @Override
    public PRStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(Status status) {
        this.status = (PRStatus) status;
    }

    @Override
    public String getModule() {
        return "PR";
    }
}

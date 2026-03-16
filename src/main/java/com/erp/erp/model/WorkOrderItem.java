package com.erp.erp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "work_order_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_order_id", nullable = false)
    @ToString.Exclude
    private WorkOrder workOrder;

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private Product material;

    private Integer plannedQuantity;

    @Builder.Default
    private Integer consumedQuantity = 0;

    @Column(columnDefinition = "TEXT")
    private String note;
}

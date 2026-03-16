package com.erp.erp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bom_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BomItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bom_id", nullable = false)
    @ToString.Exclude
    private Bom bom;

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private Product material; // Raw material needed

    private Integer quantity;

    @Column(columnDefinition = "TEXT")
    private String note;
}

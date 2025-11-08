package com.erp.erp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponse {
    private Long id; // ID kategori
    private String name; // Nama kategori
    private String description; // Deskripsi kategori
    private String imageUrl; // Link gambar kategori
    private Boolean active; // Status aktif / nonaktif
}

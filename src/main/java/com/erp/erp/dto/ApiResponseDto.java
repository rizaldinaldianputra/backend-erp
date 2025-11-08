package com.erp.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDto<T> {
    private String status; // "success", "error", "fail"
    private String message; // Keterangan tambahan
    private T data; // Payload, bisa DTO atau List<DTO>
}

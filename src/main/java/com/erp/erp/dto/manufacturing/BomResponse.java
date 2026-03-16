package com.erp.erp.dto.manufacturing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BomResponse {

    private Long id;
    private String code;
    private String name;
    private Long productId;
    private String productName;
    private Integer quantity;
    private String description;
    private String status;
    private List<BomItemResponse> items;
    private LocalDateTime createdDate;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BomItemResponse {
        private Long id;
        private Long materialId;
        private String materialName;
        private Integer quantity;
        private String note;
    }
}

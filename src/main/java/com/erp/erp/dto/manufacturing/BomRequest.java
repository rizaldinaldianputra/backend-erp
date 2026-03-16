package com.erp.erp.dto.manufacturing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BomRequest {

    private String name;
    private Long productId;
    private Integer quantity;
    private String description;
    private List<BomItemRequest> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BomItemRequest {
        private Long materialId;
        private Integer quantity;
        private String note;
    }
}

package com.erp.erp.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UomResponse {
    private Long id;
    private String code;
    private String name;
}

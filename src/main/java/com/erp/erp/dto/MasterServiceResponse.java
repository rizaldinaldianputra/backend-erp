package com.erp.erp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MasterServiceResponse {

    private Long id;
    private String code;
    private String name;
    private String description;
    private Double unitPrice;
    private Boolean active;
}

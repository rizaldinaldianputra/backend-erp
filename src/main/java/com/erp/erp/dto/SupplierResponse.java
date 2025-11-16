package com.erp.erp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SupplierResponse {
    private Long id;
    private String code;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String website;
    private String npwp;
    private Boolean active;
}

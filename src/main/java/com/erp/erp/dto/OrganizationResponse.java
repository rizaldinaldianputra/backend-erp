package com.erp.erp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrganizationResponse {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private Boolean active;
}

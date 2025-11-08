package com.erp.erp.dto;

import com.erp.erp.model.Organization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfficeResponse {

    private Long id;
    private String name;
    private String address;
    private String code;
    private String phone;
    private String email;
    private String website;
    private Organization organization;
}

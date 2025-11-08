package com.erp.erp.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseRequestResponse {
    private Long id;
    private String documentNumber;
    private LocalDateTime trxDate;
    private LocalDateTime requiredDate;
    private String notes;
    private String createdBy;
    private String status;
    private List<PurchaseRequestItemResponse> items;
}

package com.erp.erp.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PurchaseOrderResponse {

    private Long id;
    private String documentNumber;
    private LocalDateTime trxDate;
    private String notes;
    private String status;

    private PurchaseRequestResponse purchaseRequest; // optional
    private SupplierResponse supplier; // object supplier

    private UserResponse createdByUser;
    private UserResponse updatedByUser;

    private List<PurchaseOrderItemResponse> items;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}

package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.dto.manufacturing.WorkOrderRequest;
import com.erp.erp.dto.manufacturing.WorkOrderResponse;
import com.erp.erp.model.WorkOrder;
import com.erp.erp.service.WorkOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/work-orders")
@RequiredArgsConstructor
@Tag(name = "Manufacturing - Work Order", description = "Manage Production Work Orders")
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    @PostMapping
    @Operation(summary = "Create a new Work Order")
    public ResponseEntity<ApiResponseDto<WorkOrderResponse>> createWorkOrder(@RequestBody WorkOrderRequest request) {
        WorkOrderResponse response = workOrderService.createWorkOrder(request);
        return ResponseEntity.ok(ApiResponseDto.<WorkOrderResponse>builder()
                .status("success")
                .data(response)
                .build());
    }

    @GetMapping
    @Operation(summary = "Get all Work Orders")
    public ResponseEntity<ApiResponseDto<Page<WorkOrderResponse>>> getAllWorkOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponseDto.<Page<WorkOrderResponse>>builder()
                .status("success")
                .data(workOrderService.getAllWorkOrders(pageable))
                .build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Work Order by ID")
    public ResponseEntity<ApiResponseDto<WorkOrderResponse>> getWorkOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDto.<WorkOrderResponse>builder()
                .status("success")
                .data(workOrderService.getWorkOrderById(id))
                .build());
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update Work Order Status")
    public ResponseEntity<ApiResponseDto<WorkOrderResponse>> updateStatus(@PathVariable Long id,
            @RequestParam WorkOrder.Status status) {
        return ResponseEntity.ok(ApiResponseDto.<WorkOrderResponse>builder()
                .status("success")
                .data(workOrderService.updateStatus(id, status))
                .build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Work Order")
    public ResponseEntity<ApiResponseDto<Void>> deleteWorkOrder(@PathVariable Long id) {
        workOrderService.deleteWorkOrder(id);
        return ResponseEntity.ok(ApiResponseDto.<Void>builder()
                .status("success")
                .message("Work Order deleted successfully")
                .build());
    }
}

package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.dto.inventory.StockAdjustmentRequest;
import com.erp.erp.dto.inventory.StockTransferRequest;
import com.erp.erp.model.Inventory;
import com.erp.erp.model.StockMovement;
import com.erp.erp.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory", description = "Manage Stock, Opname, and Transfers")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    @Operation(summary = "Get all Inventory/Stock")
    public ResponseEntity<ApiResponseDto<List<Inventory>>> getAllInventory() {
        return ResponseEntity.ok(ApiResponseDto.<List<Inventory>>builder()
                .status("success")
                .data(inventoryService.getAllInventory())
                .build());
    }

    @GetMapping("/warehouse/{warehouseId}")
    @Operation(summary = "Get Inventory by Warehouse")
    public ResponseEntity<ApiResponseDto<List<Inventory>>> getInventoryByWarehouse(@PathVariable Long warehouseId) {
        return ResponseEntity.ok(ApiResponseDto.<List<Inventory>>builder()
                .status("success")
                .data(inventoryService.getInventoryByWarehouse(warehouseId))
                .build());
    }

    @PostMapping("/adjust")
    @Operation(summary = "Adjust Stock (Opname/Adjustment)")
    public ResponseEntity<ApiResponseDto<StockMovement>> adjustStock(@RequestBody StockAdjustmentRequest request) {
        StockMovement movement = inventoryService.adjustStock(request);
        return ResponseEntity.ok(ApiResponseDto.<StockMovement>builder()
                .status("success")
                .data(movement)
                .build());
    }

    @PostMapping("/transfer")
    @Operation(summary = "Transfer Stock between Warehouses")
    public ResponseEntity<ApiResponseDto<String>> transferStock(@RequestBody StockTransferRequest request) {
        inventoryService.transferStock(request);
        return ResponseEntity.ok(ApiResponseDto.<String>builder()
                .status("success")
                .data("Transfer successful")
                .build());
    }
}

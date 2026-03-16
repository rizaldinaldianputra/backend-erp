package com.erp.erp.controller;

import com.erp.erp.dto.purchase.PurchaseReturnRequest;
import com.erp.erp.dto.purchase.PurchaseReturnResponse;
import com.erp.erp.service.PurchaseReturnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase/returns")
@RequiredArgsConstructor
public class PurchaseReturnController {

    private final PurchaseReturnService purchaseReturnService;

    @PostMapping
    public ResponseEntity<PurchaseReturnResponse> createPurchaseReturn(@RequestBody PurchaseReturnRequest request) {
        return new ResponseEntity<>(purchaseReturnService.createPurchaseReturn(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PurchaseReturnResponse>> getAllPurchaseReturns() {
        return ResponseEntity.ok(purchaseReturnService.getAllPurchaseReturns());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseReturnResponse> getPurchaseReturnById(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseReturnService.getPurchaseReturnById(id));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<PurchaseReturnResponse> approvePurchaseReturn(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseReturnService.approvePurchaseReturn(id));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<PurchaseReturnResponse> rejectPurchaseReturn(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseReturnService.rejectPurchaseReturn(id));
    }

    @PutMapping("/{id}/process")
    public ResponseEntity<PurchaseReturnResponse> processPurchaseReturn(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseReturnService.processPurchaseReturn(id));
    }
}

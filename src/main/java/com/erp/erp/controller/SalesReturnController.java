package com.erp.erp.controller;

import com.erp.erp.dto.sales.SalesReturnRequest;
import com.erp.erp.dto.sales.SalesReturnResponse;
import com.erp.erp.service.SalesReturnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales/returns")
@RequiredArgsConstructor
public class SalesReturnController {

    private final SalesReturnService salesReturnService;

    @PostMapping
    public ResponseEntity<SalesReturnResponse> createSalesReturn(@RequestBody SalesReturnRequest request) {
        return new ResponseEntity<>(salesReturnService.createSalesReturn(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SalesReturnResponse>> getAllSalesReturns() {
        return ResponseEntity.ok(salesReturnService.getAllSalesReturns());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesReturnResponse> getSalesReturnById(@PathVariable Long id) {
        return ResponseEntity.ok(salesReturnService.getSalesReturnById(id));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<SalesReturnResponse> approveSalesReturn(@PathVariable Long id) {
        return ResponseEntity.ok(salesReturnService.approveSalesReturn(id));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<SalesReturnResponse> rejectSalesReturn(@PathVariable Long id) {
        return ResponseEntity.ok(salesReturnService.rejectSalesReturn(id));
    }

    @PutMapping("/{id}/process")
    public ResponseEntity<SalesReturnResponse> processSalesReturn(@PathVariable Long id) {
        return ResponseEntity.ok(salesReturnService.processSalesReturn(id));
    }
}

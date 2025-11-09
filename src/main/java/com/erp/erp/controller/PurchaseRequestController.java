package com.erp.erp.controller;

import com.erp.erp.dto.PurchaseRequestResponse;
import com.erp.erp.model.PurchaseRequest;
import com.erp.erp.service.PurchaseRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/purchase-requests")
public class PurchaseRequestController {

    private final PurchaseRequestService prService;

    public PurchaseRequestController(PurchaseRequestService prService) {
        this.prService = prService;
    }

    @GetMapping
    public List<PurchaseRequestResponse> getAll() {
        return prService.findAll();
    }

    @GetMapping("/{id}")
    public PurchaseRequestResponse getById(@PathVariable Long id) {
        return prService.findById(id);
    }

    @PostMapping
    public PurchaseRequest create(@RequestBody PurchaseRequest pr) {
        return prService.create(pr);
    }

    @PostMapping("/{id}/submit")
    public PurchaseRequest submit(@PathVariable Long id) {
        return prService.submit(id);
    }

    @PostMapping("/{id}/approve")
    public PurchaseRequest approve(@PathVariable Long id) {
        return prService.approve(id);
    }

    @PostMapping("/{id}/reject")
    public PurchaseRequest reject(@PathVariable Long id) {
        return prService.reject(id);
    }

}

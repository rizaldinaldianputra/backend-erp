package com.erp.erp.controller;

import com.erp.erp.dto.PurchaseRequestResponse;
import com.erp.erp.model.PurchaseRequest;
import com.erp.erp.service.PurchaseRequestService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/purchase-requests")
@Tag(name = "Purchase Request", description = "Manage company Product data")

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

}

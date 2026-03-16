package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.model.Tax;
import com.erp.erp.service.TaxService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/taxes")
@Tag(name = "Tax", description = "Manage company Taxes")
public class TaxController {

    private final TaxService taxService;

    public TaxController(TaxService taxService) {
        this.taxService = taxService;
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<Page<Tax>>> getAllTaxes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponseDto.<Page<Tax>>builder()
                .status("success")
                .message("Taxes fetched successfully")
                .data(taxService.getAllTaxes(pageable))
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Tax>> getTaxById(@PathVariable Long id) {
        return taxService.getTaxById(id)
                .map(tax -> ResponseEntity.ok(ApiResponseDto.<Tax>builder()
                        .status("success")
                        .message("Tax fetched successfully")
                        .data(tax)
                        .build()))
                .orElse(ResponseEntity.status(404).body(ApiResponseDto.<Tax>builder()
                        .status("error")
                        .message("Tax not found")
                        .data(null)
                        .build()));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<Tax>> createTax(@RequestBody Tax tax) {
        try {
            Tax created = taxService.createTax(tax);
            return ResponseEntity.ok(ApiResponseDto.<Tax>builder()
                    .status("success")
                    .message("Tax created successfully")
                    .data(created)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponseDto.<Tax>builder()
                    .status("error")
                    .message(e.getMessage())
                    .data(null)
                    .build());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Tax>> updateTax(@PathVariable Long id, @RequestBody Tax tax) {
        try {
            Tax updated = taxService.updateTax(id, tax);
            return ResponseEntity.ok(ApiResponseDto.<Tax>builder()
                    .status("success")
                    .message("Tax updated successfully")
                    .data(updated)
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(ApiResponseDto.<Tax>builder()
                    .status("error")
                    .message(e.getMessage())
                    .data(null)
                    .build());
        }
    }
}

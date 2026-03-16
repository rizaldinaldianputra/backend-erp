package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.model.Bank;
import com.erp.erp.service.BankService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/banks")
@Tag(name = "Bank", description = "Manage company Bank accounts")
public class BankController {

    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<Page<Bank>>> getAllBanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponseDto.<Page<Bank>>builder()
                .status("success")
                .message("Banks fetched successfully")
                .data(bankService.getAllBanks(pageable))
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Bank>> getBankById(@PathVariable Long id) {
        return bankService.getBankById(id)
                .map(bank -> ResponseEntity.ok(ApiResponseDto.<Bank>builder()
                        .status("success")
                        .message("Bank fetched successfully")
                        .data(bank)
                        .build()))
                .orElse(ResponseEntity.status(404).body(ApiResponseDto.<Bank>builder()
                        .status("error")
                        .message("Bank not found")
                        .data(null)
                        .build()));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<Bank>> createBank(@RequestBody Bank bank) {
        try {
            Bank created = bankService.createBank(bank);
            return ResponseEntity.ok(ApiResponseDto.<Bank>builder()
                    .status("success")
                    .message("Bank created successfully")
                    .data(created)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponseDto.<Bank>builder()
                    .status("error")
                    .message(e.getMessage())
                    .data(null)
                    .build());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Bank>> updateBank(@PathVariable Long id, @RequestBody Bank bank) {
        try {
            Bank updated = bankService.updateBank(id, bank);
            return ResponseEntity.ok(ApiResponseDto.<Bank>builder()
                    .status("success")
                    .message("Bank updated successfully")
                    .data(updated)
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(ApiResponseDto.<Bank>builder()
                    .status("error")
                    .message(e.getMessage())
                    .data(null)
                    .build());
        }
    }
}

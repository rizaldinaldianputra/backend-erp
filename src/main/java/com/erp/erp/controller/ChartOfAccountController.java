package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.model.ChartOfAccount;
import com.erp.erp.service.ChartOfAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coa")
@Tag(name = "Chart of Account", description = "Manage company Chart of Accounts")
public class ChartOfAccountController {

    private final ChartOfAccountService coaService;

    public ChartOfAccountController(ChartOfAccountService coaService) {
        this.coaService = coaService;
    }

    @GetMapping
    @Operation(summary = "Get all Accounts with pagination")
    public ResponseEntity<ApiResponseDto<Page<ChartOfAccount>>> getAllAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponseDto.<Page<ChartOfAccount>>builder()
                .status("success")
                .message("Accounts fetched successfully")
                .data(coaService.getAllAccounts(pageable))
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<ChartOfAccount>> getAccountById(@PathVariable Long id) {
        return coaService.getAccountById(id)
                .map(account -> ResponseEntity.ok(ApiResponseDto.<ChartOfAccount>builder()
                        .status("success")
                        .message("Account fetched successfully")
                        .data(account)
                        .build()))
                .orElse(ResponseEntity.status(404).body(ApiResponseDto.<ChartOfAccount>builder()
                        .status("error")
                        .message("Account not found")
                        .data(null)
                        .build()));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<ChartOfAccount>> createAccount(@RequestBody ChartOfAccount account) {
        try {
            ChartOfAccount created = coaService.createAccount(account);
            return ResponseEntity.ok(ApiResponseDto.<ChartOfAccount>builder()
                    .status("success")
                    .message("Account created successfully")
                    .data(created)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponseDto.<ChartOfAccount>builder()
                    .status("error")
                    .message(e.getMessage())
                    .data(null)
                    .build());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<ChartOfAccount>> updateAccount(@PathVariable Long id,
            @RequestBody ChartOfAccount account) {
        try {
            ChartOfAccount updated = coaService.updateAccount(id, account);
            return ResponseEntity.ok(ApiResponseDto.<ChartOfAccount>builder()
                    .status("success")
                    .message("Account updated successfully")
                    .data(updated)
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(ApiResponseDto.<ChartOfAccount>builder()
                    .status("error")
                    .message(e.getMessage())
                    .data(null)
                    .build());
        }
    }
}

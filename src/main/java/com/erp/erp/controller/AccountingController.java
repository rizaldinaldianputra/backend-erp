package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.model.JournalHeader;
import com.erp.erp.service.AccountingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounting/journals")
@Tag(name = "Accounting Journal", description = "Manage Journal Entries")
public class AccountingController {

    private final AccountingService accountingService;

    public AccountingController(AccountingService accountingService) {
        this.accountingService = accountingService;
    }

    @GetMapping
    @Operation(summary = "Get all journals with pagination")
    public ResponseEntity<ApiResponseDto<Page<JournalHeader>>> getAllJournals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponseDto.<Page<JournalHeader>>builder()
                .status("success")
                .message("Journals fetched successfully")
                .data(accountingService.getAllJournals(pageable))
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<JournalHeader>> getJournalById(@PathVariable Long id) {
        return accountingService.getJournalById(id)
                .map(journal -> ResponseEntity.ok(ApiResponseDto.<JournalHeader>builder()
                        .status("success")
                        .message("Journal fetched successfully")
                        .data(journal)
                        .build()))
                .orElse(ResponseEntity.status(404).body(ApiResponseDto.<JournalHeader>builder()
                        .status("error")
                        .message("Journal not found")
                        .data(null)
                        .build()));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<JournalHeader>> createJournal(@RequestBody JournalHeader journal) {
        try {
            JournalHeader created = accountingService.createJournal(journal);
            return ResponseEntity.ok(ApiResponseDto.<JournalHeader>builder()
                    .status("success")
                    .message("Journal created successfully")
                    .data(created)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponseDto.<JournalHeader>builder()
                    .status("error")
                    .message(e.getMessage())
                    .data(null)
                    .build());
        }
    }

    @PostMapping("/{id}/post")
    public ResponseEntity<ApiResponseDto<JournalHeader>> postJournal(@PathVariable Long id) {
        try {
            JournalHeader posted = accountingService.postJournal(id);
            return ResponseEntity.ok(ApiResponseDto.<JournalHeader>builder()
                    .status("success")
                    .message("Journal posted successfully")
                    .data(posted)
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponseDto.<JournalHeader>builder()
                    .status("error")
                    .message(e.getMessage())
                    .data(null)
                    .build());
        }
    }
}

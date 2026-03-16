package com.erp.erp.controller;

import com.erp.erp.dto.finance.FixedAssetRequest;
import com.erp.erp.dto.finance.FixedAssetResponse;
import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.service.FixedAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fixed-assets")
@RequiredArgsConstructor
public class FixedAssetController {

    private final FixedAssetService fixedAssetService;

    @PostMapping
    public ResponseEntity<ApiResponseDto<FixedAssetResponse>> createFixedAsset(@RequestBody FixedAssetRequest request) {
        FixedAssetResponse response = fixedAssetService.createFixedAsset(request);
        return ResponseEntity.ok(ApiResponseDto.<FixedAssetResponse>builder()
                .status("success")
                .message("Fixed Asset created successfully")
                .data(response)
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<Page<FixedAssetResponse>>> getAllFixedAssets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponseDto.<Page<FixedAssetResponse>>builder()
                .status("success")
                .message("Success")
                .data(fixedAssetService.getAllFixedAssets(pageable))
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<FixedAssetResponse>> getFixedAssetById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDto.<FixedAssetResponse>builder()
                .status("success")
                .message("Success")
                .data(fixedAssetService.getFixedAssetById(id))
                .build());
    }

    @PostMapping("/{id}/dispose")
    public ResponseEntity<ApiResponseDto<FixedAssetResponse>> disposeFixedAsset(@PathVariable Long id) {
        FixedAssetResponse response = fixedAssetService.disposeFixedAsset(id);
        return ResponseEntity.ok(ApiResponseDto.<FixedAssetResponse>builder()
                .status("success")
                .message("Fixed Asset disposed successfully")
                .data(response)
                .build());
    }
}

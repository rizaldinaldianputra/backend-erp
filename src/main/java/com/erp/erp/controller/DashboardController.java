package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.service.DashboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Dashboard and Analytics")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'SUPERADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> getDashboardStats() {
        Map<String, Object> stats = dashboardService.getDashboardStats();
        return ResponseEntity.ok(ApiResponseDto.<Map<String, Object>>builder()
                .status("success")
                .message("Dashboard stats fetched")
                .data(stats)
                .build());
    }

    @GetMapping("/charts/production-vs-orders")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'SUPERADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponseDto<List<Map<String, Object>>>> getProductionVsOrders() {
        return ResponseEntity.ok(ApiResponseDto.<List<Map<String, Object>>>builder()
                .status("success")
                .data(dashboardService.getProductionVsOrdersData())
                .build());
    }

    @GetMapping("/charts/revenue-vs-cost")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'SUPERADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponseDto<List<Map<String, Object>>>> getRevenueVsCost() {
        return ResponseEntity.ok(ApiResponseDto.<List<Map<String, Object>>>builder()
                .status("success")
                .data(dashboardService.getRevenueVsCostData())
                .build());
    }

    @GetMapping("/charts/product-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'SUPERADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponseDto<List<Map<String, Object>>>> getProductDistribution() {
        return ResponseEntity.ok(ApiResponseDto.<List<Map<String, Object>>>builder()
                .status("success")
                .data(dashboardService.getProductDistributionData())
                .build());
    }
}

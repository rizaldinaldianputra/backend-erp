package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.dto.project.TimesheetRequest;
import com.erp.erp.dto.project.TimesheetResponse;
import com.erp.erp.model.Timesheet;
import com.erp.erp.service.TimesheetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/timesheets")
@RequiredArgsConstructor
@Tag(name = "Timesheet", description = "Manage Employee Timesheets")
public class TimesheetController {

    private final TimesheetService timesheetService;

    @PostMapping
    @Operation(summary = "Submit a new Timesheet")
    public ResponseEntity<ApiResponseDto<TimesheetResponse>> createTimesheet(@RequestBody TimesheetRequest request) {
        TimesheetResponse response = timesheetService.createTimesheet(request);
        return ResponseEntity.ok(ApiResponseDto.<TimesheetResponse>builder()
                .status("success")
                .data(response)
                .build());
    }

    @GetMapping
    @Operation(summary = "Get all Timesheets with pagination")
    public ResponseEntity<ApiResponseDto<Page<TimesheetResponse>>> getAllTimesheets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponseDto.<Page<TimesheetResponse>>builder()
                .status("success")
                .data(timesheetService.getAllTimesheets(pageable))
                .build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Timesheet by ID")
    public ResponseEntity<ApiResponseDto<TimesheetResponse>> getTimesheetById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDto.<TimesheetResponse>builder()
                .status("success")
                .data(timesheetService.getTimesheetById(id))
                .build());
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update Timesheet Status")
    public ResponseEntity<ApiResponseDto<TimesheetResponse>> updateStatus(@PathVariable Long id,
            @RequestParam Timesheet.Status status) {
        return ResponseEntity.ok(ApiResponseDto.<TimesheetResponse>builder()
                .status("success")
                .data(timesheetService.updateStatus(id, status))
                .build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Timesheet")
    public ResponseEntity<ApiResponseDto<Void>> deleteTimesheet(@PathVariable Long id) {
        timesheetService.deleteTimesheet(id);
        return ResponseEntity.ok(ApiResponseDto.<Void>builder()
                .status("success")
                .message("Timesheet deleted successfully")
                .build());
    }
}

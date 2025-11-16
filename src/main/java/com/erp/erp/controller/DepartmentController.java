package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.dto.DepartmentResponse;
import com.erp.erp.model.Department;
import com.erp.erp.service.DepartmentService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/departments")
@Tag(name = "Department", description = "Manage departments")
public class DepartmentController {

        private final DepartmentService departmentService;

        public DepartmentController(DepartmentService service) {
                this.departmentService = service;
        }

        private DepartmentResponse mapToResponse(Department d) {
                return DepartmentResponse.builder()
                                .id(d.getId())
                                .code(d.getCode())
                                .name(d.getName())
                                .office(d.getOffice())
                                .build();
        }

        // GET ALL
        // GET ALL WITH PAGINATION
        @GetMapping
        public ResponseEntity<ApiResponseDto<Page<DepartmentResponse>>> getAllDepartments(
                        @PageableDefault(size = 10) Pageable pageable) {

                Page<DepartmentResponse> paged = departmentService.getAllDepartments(pageable)
                                .map(this::mapToResponse);

                return ResponseEntity.ok(
                                ApiResponseDto.<Page<DepartmentResponse>>builder()
                                                .status("success")
                                                .message("Departments fetched successfully")
                                                .data(paged)
                                                .build());
        }

        // GET BY ID
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponseDto<DepartmentResponse>> getById(@PathVariable Long id) {
                return departmentService.getDepartmentById(id)
                                .map(d -> ResponseEntity.ok(
                                                ApiResponseDto.<DepartmentResponse>builder()
                                                                .status("success")
                                                                .message("Department fetched successfully")
                                                                .data(mapToResponse(d))
                                                                .build()))
                                .orElse(ResponseEntity.status(404).body(
                                                ApiResponseDto.<DepartmentResponse>builder()
                                                                .status("error")
                                                                .message("Department not found")
                                                                .data(null)
                                                                .build()));
        }

        // CREATE
        @PostMapping
        public ResponseEntity<ApiResponseDto<DepartmentResponse>> create(@RequestBody Department dept) {
                Department created = departmentService.createDepartment(dept);

                return ResponseEntity.ok(
                                ApiResponseDto.<DepartmentResponse>builder()
                                                .status("success")
                                                .message("Department created successfully")
                                                .data(mapToResponse(created))
                                                .build());
        }

        // UPDATE
        @PutMapping("/{id}")
        public ResponseEntity<ApiResponseDto<DepartmentResponse>> update(
                        @PathVariable Long id, @RequestBody Department dept) {

                Department updated = departmentService.updateDepartment(id, dept);

                return ResponseEntity.ok(
                                ApiResponseDto.<DepartmentResponse>builder()
                                                .status("success")
                                                .message("Department updated successfully")
                                                .data(mapToResponse(updated))
                                                .build());
        }

}

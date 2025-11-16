package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.dto.EmployeeResponse;
import com.erp.erp.model.Employee;
import com.erp.erp.service.EmployeeService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employee", description = "Manage employee data")
public class EmployeeController {

        private final EmployeeService employeeService;

        public EmployeeController(EmployeeService service) {
                this.employeeService = service;
        }

        private EmployeeResponse mapToResponse(Employee e) {
                return EmployeeResponse.builder()
                                .id(e.getId())
                                .employeeCode(e.getEmployeeCode())
                                .firstName(e.getFirstName())
                                .lastName(e.getLastName())
                                .birthDate(e.getBirthDate())
                                .birthPlace(e.getBirthPlace())
                                .gender(e.getGender())
                                .nik(e.getNik())
                                .kkNumber(e.getKkNumber())
                                .joinDate(e.getJoinDate())
                                .department(e.getDepartment())
                                .position(e.getPosition())
                                .phone(e.getPhone())
                                .email(e.getEmail())
                                .address(e.getAddress())
                                .lastEducation(e.getLastEducation())
                                .user(e.getUser())
                                .build();
        }

        @GetMapping
        public ResponseEntity<ApiResponseDto<Page<EmployeeResponse>>> getAll(
                        @PageableDefault(size = 10) Pageable pageable) {

                Page<EmployeeResponse> paged = employeeService.getAllEmployees(pageable)
                                .map(this::mapToResponse);

                return ResponseEntity.ok(
                                ApiResponseDto.<Page<EmployeeResponse>>builder()
                                                .status("success")
                                                .message("Employees fetched successfully")
                                                .data(paged)
                                                .build());
        }

        @GetMapping("/{id}")
        public ResponseEntity<ApiResponseDto<EmployeeResponse>> getById(@PathVariable Long id) {
                return employeeService.getEmployeeById(id)
                                .map(e -> ResponseEntity.ok(
                                                ApiResponseDto.<EmployeeResponse>builder()
                                                                .status("success")
                                                                .message("Employee fetched successfully")
                                                                .data(mapToResponse(e))
                                                                .build()))
                                .orElse(ResponseEntity.status(404).body(
                                                ApiResponseDto.<EmployeeResponse>builder()
                                                                .status("error")
                                                                .message("Employee not found")
                                                                .data(null)
                                                                .build()));
        }

        @PostMapping
        public ResponseEntity<ApiResponseDto<EmployeeResponse>> create(@RequestBody Employee emp) {
                Employee created = employeeService.createEmployee(emp);

                return ResponseEntity.ok(
                                ApiResponseDto.<EmployeeResponse>builder()
                                                .status("success")
                                                .message("Employee created successfully")
                                                .data(mapToResponse(created))
                                                .build());
        }

        @PutMapping("/{id}")
        public ResponseEntity<ApiResponseDto<EmployeeResponse>> update(
                        @PathVariable Long id, @RequestBody Employee emp) {

                Employee updated = employeeService.updateEmployee(id, emp);

                return ResponseEntity.ok(
                                ApiResponseDto.<EmployeeResponse>builder()
                                                .status("success")
                                                .message("Employee updated successfully")
                                                .data(mapToResponse(updated))
                                                .build());
        }
}

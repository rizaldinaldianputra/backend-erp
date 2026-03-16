package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.model.Customer;
import com.erp.erp.service.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customer", description = "Manage company Customer data")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<Page<Customer>>> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customers = customerService.getAllCustomers(pageable);
        return ResponseEntity.ok(ApiResponseDto.<Page<Customer>>builder()
                .status("success")
                .message("Customers fetched successfully")
                .data(customers)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Customer>> getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id)
                .map(customer -> ResponseEntity.ok(ApiResponseDto.<Customer>builder()
                        .status("success")
                        .message("Customer fetched successfully")
                        .data(customer)
                        .build()))
                .orElse(ResponseEntity.status(404).body(ApiResponseDto.<Customer>builder()
                        .status("error")
                        .message("Customer not found")
                        .data(null)
                        .build()));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<Customer>> createCustomer(@RequestBody Customer customer) {
        try {
            Customer created = customerService.createCustomer(customer);
            return ResponseEntity.ok(ApiResponseDto.<Customer>builder()
                    .status("success")
                    .message("Customer created successfully")
                    .data(created)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponseDto.<Customer>builder()
                    .status("error")
                    .message(e.getMessage())
                    .data(null)
                    .build());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Customer>> updateCustomer(@PathVariable Long id,
            @RequestBody Customer customer) {
        try {
            Customer updated = customerService.updateCustomer(id, customer);
            return ResponseEntity.ok(ApiResponseDto.<Customer>builder()
                    .status("success")
                    .message("Customer updated successfully")
                    .data(updated)
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(ApiResponseDto.<Customer>builder()
                    .status("error")
                    .message(e.getMessage())
                    .data(null)
                    .build());
        }
    }
}

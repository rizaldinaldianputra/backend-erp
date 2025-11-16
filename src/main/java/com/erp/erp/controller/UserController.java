package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.dto.UserResponse;
import com.erp.erp.model.User;
import com.erp.erp.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "Manage application users")
public class UserController {

        private final UserService userService;

        public UserController(UserService userService) {
                this.userService = userService;
        }

        // Mapping User entity ke UserResponse DTO
        private UserResponse mapToResponse(User user) {
                return UserResponse.builder()
                                .id(user.getId())
                                .username(user.getUsername())
                                .fullName(user.getFullName())
                                .email(user.getEmail())
                                .supervisorId(user.getSupervisor() != null ? user.getSupervisor().getId() : null)
                                .active(true) // bisa diubah sesuai field entity
                                .build();
        }

        // GET all users
        @GetMapping
        public ResponseEntity<ApiResponseDto<List<UserResponse>>> getAllUsers() {
                List<UserResponse> users = userService.getAllUsers()
                                .stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());

                return ResponseEntity.ok(
                                ApiResponseDto.<List<UserResponse>>builder()
                                                .status("success")
                                                .message("Users fetched successfully")
                                                .data(users)
                                                .build());
        }

        // GET user by ID
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponseDto<UserResponse>> getUserById(@PathVariable Long id) {
                return userService.getUserById(id)
                                .map(user -> ResponseEntity.ok(
                                                ApiResponseDto.<UserResponse>builder()
                                                                .status("success")
                                                                .message("User fetched successfully")
                                                                .data(mapToResponse(user))
                                                                .build()))
                                .orElse(ResponseEntity.status(404).body(
                                                ApiResponseDto.<UserResponse>builder()
                                                                .status("error")
                                                                .message("User not found")
                                                                .data(null)
                                                                .build()));
        }

        // POST create new user
        @PostMapping
        public ResponseEntity<ApiResponseDto<UserResponse>> createUser(@RequestBody User user) {
                User created = userService.createUser(user);
                return ResponseEntity.ok(
                                ApiResponseDto.<UserResponse>builder()
                                                .status("success")
                                                .message("User created successfully")
                                                .data(mapToResponse(created))
                                                .build());
        }

        // PUT update user
        @PutMapping("/{id}")
        public ResponseEntity<ApiResponseDto<UserResponse>> updateUser(
                        @PathVariable Long id,
                        @RequestBody User user) {
                User updated = userService.updateUser(id, user);
                return ResponseEntity.ok(
                                ApiResponseDto.<UserResponse>builder()
                                                .status("success")
                                                .message("User updated successfully")
                                                .data(mapToResponse(updated))
                                                .build());
        }

}

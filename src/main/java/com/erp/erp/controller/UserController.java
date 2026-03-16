package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.dto.UserResponse;
import com.erp.erp.model.User;
import com.erp.erp.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "Manage application users")
public class UserController {

        private final UserService userService;
        private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

        public UserController(UserService userService,
                        org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
                this.userService = userService;
                this.passwordEncoder = passwordEncoder;
        }

        // Mapping User entity ke UserResponse DTO
        private UserResponse mapToResponse(User user) {
                return UserResponse.builder()
                                .id(user.getId())
                                .username(user.getUsername())
                                .fullName(user.getFullName())
                                .email(user.getEmail())
                                .supervisorId(user.getSupervisor() != null ? user.getSupervisor().getId() : null)
                                .avatarUrl(user.getAvatarUrl())
                                .role(user.getRole())
                                .organizationId(user.getOrganization() != null ? user.getOrganization().getId() : null)
                                .organizationName(user.getOrganization() != null ? user.getOrganization().getName()
                                                : null)
                                .active(true) // bisa diubah sesuai field entity
                                .build();
        }

        // GET all users — accessible by any admin-level role
        @GetMapping
        @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'SUPERADMIN', 'MANAGER', 'HR')")
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

        @PostMapping
        @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'SUPERADMIN', 'MANAGER', 'HR')")
        public ResponseEntity<ApiResponseDto<UserResponse>> createUser(@RequestBody User user) {
                if (user.getPassword() != null) {
                        user.setPassword(passwordEncoder.encode(user.getPassword()));
                }
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
        @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN') or @securityUtil.getCurrentUsername() == principal.username")
        public ResponseEntity<ApiResponseDto<UserResponse>> updateUser(
                        @PathVariable Long id,
                        @RequestBody User user) {
                if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                        user.setPassword(passwordEncoder.encode(user.getPassword()));
                }
                User updated = userService.updateUser(id, user);
                return ResponseEntity.ok(
                                ApiResponseDto.<UserResponse>builder()
                                                .status("success")
                                                .message("User updated successfully")
                                                .data(mapToResponse(updated))
                                                .build());
        }

        @GetMapping("/me")
        public ResponseEntity<ApiResponseDto<UserResponse>> getCurrentUser() {
                String username = SecurityContextHolder.getContext()
                                .getAuthentication().getName();
                return userService.findByUsername(username)
                                .map(user -> ResponseEntity.ok(
                                                ApiResponseDto.<UserResponse>builder()
                                                                .status("success")
                                                                .message("Current user fetched")
                                                                .data(mapToResponse(user))
                                                                .build()))
                                .orElse(ResponseEntity.status(404).build());
        }

        // Debug endpoint: returns the raw authorities of the current user
        @GetMapping("/me/authorities")
        public ResponseEntity<Map<String, Object>> getCurrentUserAuthorities() {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                List<String> authorities = auth.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList());
                return ResponseEntity.ok(Map.of(
                                "username", auth.getName(),
                                "authorities", authorities));
        }

}

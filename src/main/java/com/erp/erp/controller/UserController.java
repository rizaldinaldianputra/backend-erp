package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.model.User;
import com.erp.erp.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

        private final UserService userService;

        public UserController(UserService userService) {
                this.userService = userService;
        }

        // Mapping User entity ke response (sama entity, tapi bisa filter data)
        private User mapToResponse(User user) {
                return user; // langsung kembalikan entity
        }

        // GET all users
        @GetMapping
        public ResponseEntity<ApiResponseDto<List<User>>> getAllUsers() {
                List<User> users = userService.getAllUsers()
                                .stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());

                return ResponseEntity.ok(
                                ApiResponseDto.<List<User>>builder()
                                                .status("success")
                                                .message("Users fetched successfully")
                                                .data(users)
                                                .build());
        }

        // GET user by ID
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponseDto<User>> getUserById(@PathVariable Long id) {
                return userService.getUserById(id)
                                .map(user -> ResponseEntity.ok(
                                                ApiResponseDto.<User>builder()
                                                                .status("success")
                                                                .message("User fetched successfully")
                                                                .data(mapToResponse(user))
                                                                .build()))
                                .orElse(ResponseEntity.status(404).body(
                                                ApiResponseDto.<User>builder()
                                                                .status("error")
                                                                .message("User not found")
                                                                .data(null)
                                                                .build()));
        }

        // POST create user
        @PostMapping
        public ResponseEntity<ApiResponseDto<User>> createUser(@RequestBody User user) {
                User created = userService.createUser(user);
                return ResponseEntity.ok(
                                ApiResponseDto.<User>builder()
                                                .status("success")
                                                .message("User created successfully")
                                                .data(mapToResponse(created))
                                                .build());
        }

        // PUT update user
        @PutMapping("/{id}")
        public ResponseEntity<ApiResponseDto<User>> updateUser(
                        @PathVariable Long id,
                        @RequestBody User user) {
                User updated = userService.updateUser(id, user);
                return ResponseEntity.ok(
                                ApiResponseDto.<User>builder()
                                                .status("success")
                                                .message("User updated successfully")
                                                .data(mapToResponse(updated))
                                                .build());
        }

}

package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.model.User;
import com.erp.erp.security.JwtUtil;
import com.erp.erp.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

import java.time.LocalDateTime;

@RestController
@Tag(name = "Authentication", description = "Auth")

@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto<User>> register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedDate(LocalDateTime.now());
        user.setRole("USER"); // default role
        User saved = userService.createUser(user);
        return ResponseEntity.ok(
                ApiResponseDto.<User>builder()
                        .status("success")
                        .message("User registered")
                        .data(saved)
                        .build());
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<Map<String, String>>> login(@RequestBody User loginRequest) {
        return userService.findByUsername(loginRequest.getUsername())
                .map(user -> {
                    if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                        String token = jwtUtil.generateToken(user.getUsername());
                        Map<String, String> tokenData = new HashMap<>();
                        tokenData.put("token", token);

                        return ResponseEntity.ok(
                                ApiResponseDto.<Map<String, String>>builder()
                                        .status("success")
                                        .message("Login successful")
                                        .data(tokenData)
                                        .build());
                    } else {
                        return ResponseEntity.status(401).body(
                                ApiResponseDto.<Map<String, String>>builder()
                                        .status("error")
                                        .message("Invalid password")
                                        .data(null)
                                        .build());
                    }
                }).orElse(ResponseEntity.status(404).body(
                        ApiResponseDto.<Map<String, String>>builder()
                                .status("error")
                                .message("User not found")
                                .data(null)
                                .build()));
    }

}

package com.erp.erp.controller;

import com.erp.erp.config.security.JwtUtil;
import com.erp.erp.dto.ApiResponse;
import com.erp.erp.model.User;
import com.erp.erp.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
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
    public ResponseEntity<ApiResponse<User>> register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedDate(LocalDateTime.now());
        user.setRole("USER"); // default role
        User saved = userService.createUser(user);
        return ResponseEntity.ok(
                ApiResponse.<User>builder()
                        .status("success")
                        .message("User registered")
                        .data(saved)
                        .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody User loginRequest) {
        return userService.findByUsername(loginRequest.getUsername())
                .map(user -> {
                    if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                        String token = jwtUtil.generateToken(user.getUsername());
                        return ResponseEntity.ok(
                                ApiResponse.<String>builder()
                                        .status("success")
                                        .message("Login successful")
                                        .data(token)
                                        .build()
                        );
                    } else {
                        return ResponseEntity.status(401).body(
                                ApiResponse.<String>builder()
                                        .status("error")
                                        .message("Invalid password")
                                        .data(null)
                                        .build()
                        );
                    }
                }).orElse(ResponseEntity.status(404).body(
                        ApiResponse.<String>builder()
                                .status("error")
                                .message("User not found")
                                .data(null)
                                .build()
                ));
    }
}

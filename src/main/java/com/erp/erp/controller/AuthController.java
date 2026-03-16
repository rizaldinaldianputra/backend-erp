package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.dto.ForgotPasswordRequest;
import com.erp.erp.dto.ResetPasswordRequest;
import com.erp.erp.dto.VerifyOtpRequest;
import com.erp.erp.model.User;
import com.erp.erp.security.JwtUtil;
import com.erp.erp.service.EmailService;
import com.erp.erp.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@Tag(name = "Authentication", description = "Auth")

@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public AuthController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder,
            EmailService emailService) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
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
                        tokenData.put("role", user.getRole() != null ? user.getRole() : "USER");
                        tokenData.put("username", user.getUsername());

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

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponseDto<String>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        Optional<User> userOpt = userService.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body(
                    ApiResponseDto.<String>builder()
                            .status("error")
                            .message("Email not found")
                            .data(null)
                            .build());
        }

        User user = userOpt.get();
        String otp = userService.generateOtp(user);

        // Send Email
        emailService.sendOtpEmail(user.getEmail(), otp);

        return ResponseEntity.ok(
                ApiResponseDto.<String>builder()
                        .status("success")
                        .message("OTP has been sent to your email")
                        .data(null)
                        .build());
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponseDto<String>> resendOtp(@RequestBody ForgotPasswordRequest request) {
        Optional<User> userOpt = userService.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body(
                    ApiResponseDto.<String>builder()
                            .status("error")
                            .message("Email not found")
                            .data(null)
                            .build());
        }

        User user = userOpt.get();
        String otp = userService.resendOtp(user);
        emailService.sendOtpEmail(user.getEmail(), otp);

        return ResponseEntity.ok(
                ApiResponseDto.<String>builder()
                        .status("success")
                        .message("OTP has been resent successfully")
                        .data(null)
                        .build());
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponseDto<String>> verifyOtp(@RequestBody VerifyOtpRequest request) {
        Optional<User> userOpt = userService.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body(
                    ApiResponseDto.<String>builder()
                            .status("error")
                            .message("Email not found")
                            .data(null)
                            .build());
        }

        User user = userOpt.get();
        boolean isValid = userService.verifyOtp(user, request.getOtp());

        if (!isValid) {
            return ResponseEntity.status(400).body(
                    ApiResponseDto.<String>builder()
                            .status("error")
                            .message("Invalid or expired OTP")
                            .data(null)
                            .build());
        }

        return ResponseEntity.ok(
                ApiResponseDto.<String>builder()
                        .status("success")
                        .message("OTP verified successfully")
                        .data(null)
                        .build());
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponseDto<String>> resetPassword(@RequestBody ResetPasswordRequest request) {
        Optional<User> userOpt = userService.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body(
                    ApiResponseDto.<String>builder()
                            .status("error")
                            .message("Email not found")
                            .data(null)
                            .build());
        }

        User user = userOpt.get();

        // Double check OTP when doing actual reset
        boolean isValid = userService.verifyOtp(user, request.getOtp());
        if (!isValid) {
            return ResponseEntity.status(400).body(
                    ApiResponseDto.<String>builder()
                            .status("error")
                            .message("Invalid or expired OTP")
                            .data(null)
                            .build());
        }

        userService.updatePassword(user, passwordEncoder.encode(request.getNewPassword()));

        return ResponseEntity.ok(
                ApiResponseDto.<String>builder()
                        .status("success")
                        .message("Password has been reset successfully")
                        .data(null)
                        .build());
    }
}

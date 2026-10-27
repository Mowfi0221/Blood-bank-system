package com.bloodbank.controller;

import com.bloodbank.dto.*;
import com.bloodbank.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/donor/login")
    public ResponseEntity<ApiResponse<AuthResponse>> donorLogin(@Valid @RequestBody LoginRequest req) {
        AuthResponse response = authService.donorLogin(req);
        return ResponseEntity.ok(ApiResponse.ok("Login successful", response));
    }

    @PostMapping("/admin/login")
    public ResponseEntity<ApiResponse<AuthResponse>> adminLogin(@Valid @RequestBody AdminLoginRequest req) {
        AuthResponse response = authService.adminLogin(req);
        return ResponseEntity.ok(ApiResponse.ok("Administrator login successful", response));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Object>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest req) {
        ApiResponse<Object> response = authService.requestPasswordReset(req);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Object>> verifyOtp(@Valid @RequestBody VerifyOtpRequest req) {
        ApiResponse<Object> response = authService.verifyOtp(req);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Object>> resetPassword(@Valid @RequestBody ResetPasswordRequest req) {
        ApiResponse<Object> response = authService.resetPassword(req);
        return ResponseEntity.ok(response);
    }
}

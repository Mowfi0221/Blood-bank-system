package com.bloodbank.service;

import com.bloodbank.dto.*;
import com.bloodbank.exception.BadRequestException;
import com.bloodbank.exception.UnauthorizedException;
import com.bloodbank.model.Admin;
import com.bloodbank.model.Donor;
import com.bloodbank.model.PasswordResetOtp;
import com.bloodbank.repository.AdminRepository;
import com.bloodbank.repository.DonorRepository;
import com.bloodbank.repository.PasswordResetOtpRepository;
import com.bloodbank.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class AuthService {

    private final DonorRepository donorRepository;
    private final AdminRepository adminRepository;
    private final PasswordResetOtpRepository otpRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final EmailService emailService;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${app.otp.expiration-minutes:5}")
    private int otpExpirationMinutes;

    @Value("${app.otp.max-attempts:3}")
    private int maxOtpAttempts;

    public AuthService(DonorRepository donorRepository,
                       AdminRepository adminRepository,
                       PasswordResetOtpRepository otpRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider tokenProvider,
                       EmailService emailService) {
        this.donorRepository = donorRepository;
        this.adminRepository = adminRepository;
        this.otpRepository = otpRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.emailService = emailService;
    }

    public AuthResponse donorLogin(LoginRequest req) {
        String phone = req.getPhone().trim();
        Donor donor = donorRepository.findByPhone(phone)
                .orElseThrow(() -> new UnauthorizedException("Invalid phone number or password"));

        if (!passwordEncoder.matches(req.getPassword(), donor.getPasswordHash())) {
            throw new UnauthorizedException("Invalid phone number or password");
        }

        String token = tokenProvider.generateToken(donor.getPhone(), "ROLE_DONOR", donor.getId());
        return AuthResponse.forDonor(token, DonorResponse.fromDonor(donor), "Login successful");
    }

    public AuthResponse adminLogin(AdminLoginRequest req) {
        String username = req.getUsername().trim();
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("Invalid admin credentials"));

        if (!passwordEncoder.matches(req.getPassword(), admin.getPasswordHash())) {
            throw new UnauthorizedException("Invalid admin credentials");
        }

        String token = tokenProvider.generateToken(admin.getUsername(), "ROLE_ADMIN", admin.getId());
        return AuthResponse.forAdmin(token, admin.getUsername(), "Administrator login successful");
    }

    public ApiResponse<Object> requestPasswordReset(ForgotPasswordRequest req) {
        String id = req.getIdentifier().trim();
        Optional<Donor> donorOpt = id.contains("@")
                ? donorRepository.findByEmail(id.toLowerCase())
                : donorRepository.findByPhone(id);

        if (donorOpt.isEmpty()) {
            throw new BadRequestException("No registered donor account found with provided phone number or email");
        }

        Donor donor = donorOpt.get();
        String generatedOtp = String.format("%06d", secureRandom.nextInt(1000000));
        Instant expiresAt = Instant.now().plus(otpExpirationMinutes, ChronoUnit.MINUTES);

        PasswordResetOtp otpRecord = new PasswordResetOtp(donor.getEmail(), donor.getPhone(), generatedOtp, expiresAt);
        otpRepository.save(otpRecord);

        emailService.sendOtpEmail(donor.getEmail(), generatedOtp);

        // Mask email for security: j***e@domain.com
        String email = donor.getEmail();
        String maskedEmail = maskEmail(email);

        return ApiResponse.ok("A 6-digit OTP has been sent to your registered email address (" + maskedEmail + "). Valid for 5 minutes.");
    }

    public ApiResponse<Object> verifyOtp(VerifyOtpRequest req) {
        String id = req.getIdentifier().trim();
        PasswordResetOtp otpRecord = findLatestOtp(id)
                .orElseThrow(() -> new BadRequestException("No active OTP request found. Please request a new OTP."));

        if (Instant.now().isAfter(otpRecord.getExpiresAt())) {
            throw new BadRequestException("The OTP has expired. Please request a new one.");
        }

        if (otpRecord.getAttempts() >= maxOtpAttempts) {
            throw new BadRequestException("Maximum verification attempts reached. Please request a new OTP.");
        }

        if (!otpRecord.getOtp().equals(req.getOtp().trim())) {
            otpRecord.setAttempts(otpRecord.getAttempts() + 1);
            otpRepository.save(otpRecord);
            int remaining = maxOtpAttempts - otpRecord.getAttempts();
            throw new BadRequestException("Invalid OTP code. " + (remaining > 0 ? remaining + " attempt(s) remaining." : "Please request a new OTP."));
        }

        otpRecord.setVerified(true);
        otpRepository.save(otpRecord);

        return ApiResponse.ok("OTP verified successfully. You may now reset your password.");
    }

    public ApiResponse<Object> resetPassword(ResetPasswordRequest req) {
        if (!req.getNewPassword().equals(req.getConfirmPassword())) {
            throw new BadRequestException("Password and Confirm Password do not match");
        }

        String id = req.getIdentifier().trim();
        PasswordResetOtp otpRecord = findLatestOtp(id)
                .orElseThrow(() -> new BadRequestException("No active OTP request found. Please restart the reset process."));

        if (!otpRecord.isVerified()) {
            throw new BadRequestException("OTP has not been verified yet.");
        }

        if (Instant.now().isAfter(otpRecord.getExpiresAt())) {
            throw new BadRequestException("Verification session has expired. Please request a new OTP.");
        }

        Donor donor = (id.contains("@")
                ? donorRepository.findByEmail(id.toLowerCase())
                : donorRepository.findByPhone(id))
                .orElseThrow(() -> new BadRequestException("Donor account not found"));

        donor.setPasswordHash(passwordEncoder.encode(req.getNewPassword()));
        donorRepository.save(donor);

        // Clean up OTPs for this donor
        otpRepository.deleteAllByEmail(donor.getEmail());
        otpRepository.deleteAllByPhone(donor.getPhone());

        return ApiResponse.ok("Password has been reset successfully! You can now log in with your new password.");
    }

    private Optional<PasswordResetOtp> findLatestOtp(String identifier) {
        if (identifier.contains("@")) {
            return otpRepository.findTopByEmailOrderByCreatedAtDesc(identifier.toLowerCase());
        } else {
            return otpRepository.findTopByPhoneOrderByCreatedAtDesc(identifier);
        }
    }

    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) return "registered email";
        String[] parts = email.split("@");
        String user = parts[0];
        if (user.length() <= 2) {
            return user.charAt(0) + "***@" + parts[1];
        }
        return user.charAt(0) + "***" + user.charAt(user.length() - 1) + "@" + parts[1];
    }
}

package com.bloodbank.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "password_reset_otps")
public class PasswordResetOtp {

    @Id
    private String id;

    @Field("email")
    private String email;

    @Field("phone")
    private String phone;

    @Field("otp")
    private String otp;

    @Field("expires_at")
    private Instant expiresAt;

    @Field("attempts")
    private int attempts = 0;

    @Field("verified")
    private boolean verified = false;

    @Field("created_at")
    private Instant createdAt = Instant.now();

    public PasswordResetOtp() {
    }

    public PasswordResetOtp(String email, String phone, String otp, Instant expiresAt) {
        this.email = email;
        this.phone = phone;
        this.otp = otp;
        this.expiresAt = expiresAt;
        this.attempts = 0;
        this.verified = false;
        this.createdAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}

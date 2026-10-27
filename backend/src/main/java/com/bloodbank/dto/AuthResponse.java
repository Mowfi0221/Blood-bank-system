package com.bloodbank.dto;

public class AuthResponse {

    private String token;
    private String tokenType = "Bearer";
    private String role; // "ROLE_DONOR" or "ROLE_ADMIN"
    private DonorResponse donor;
    private String username;
    private String message;

    public AuthResponse() {
    }

    public static AuthResponse forDonor(String token, DonorResponse donor, String message) {
        AuthResponse res = new AuthResponse();
        res.setToken(token);
        res.setRole("ROLE_DONOR");
        res.setDonor(donor);
        res.setMessage(message);
        return res;
    }

    public static AuthResponse forAdmin(String token, String username, String message) {
        AuthResponse res = new AuthResponse();
        res.setToken(token);
        res.setRole("ROLE_ADMIN");
        res.setUsername(username);
        res.setMessage(message);
        return res;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public DonorResponse getDonor() {
        return donor;
    }

    public void setDonor(DonorResponse donor) {
        this.donor = donor;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

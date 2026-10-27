package com.bloodbank.controller;

import com.bloodbank.dto.*;
import com.bloodbank.service.DonorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donors")
public class DonorController {

    private final DonorService donorService;

    public DonorController(DonorService donorService) {
        this.donorService = donorService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<DonorResponse>> registerDonor(@Valid @RequestBody DonorRegisterRequest req) {
        DonorResponse donor = donorService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Donor registered successfully! Welcome to the Blood Bank family.", donor));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<DonorResponse>> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        DonorResponse donor = donorService.getMe(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok("Donor profile retrieved successfully", donor));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<DonorResponse>> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody DonorUpdateRequest req) {
        DonorResponse updated = donorService.updateMe(userDetails.getUsername(), req);
        return ResponseEntity.ok(ApiResponse.ok("Donor profile updated successfully", updated));
    }

    @PatchMapping("/me/availability")
    public ResponseEntity<ApiResponse<DonorResponse>> updateAvailability(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody AvailabilityUpdateRequest req) {
        DonorResponse updated = donorService.updateAvailability(userDetails.getUsername(), req);
        return ResponseEntity.ok(ApiResponse.ok("Availability status updated successfully to " + updated.getAvailability(), updated));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<DonorPublicResponse>>> searchDonors(
            @RequestParam(required = false) String bloodGroup,
            @RequestParam(required = false) String city) {
        List<DonorPublicResponse> results = donorService.searchPublicDonors(bloodGroup, city);
        return ResponseEntity.ok(ApiResponse.ok("Available donors retrieved successfully", results));
    }
}

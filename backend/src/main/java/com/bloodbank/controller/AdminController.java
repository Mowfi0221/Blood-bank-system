package com.bloodbank.controller;

import com.bloodbank.dto.ApiResponse;
import com.bloodbank.dto.DonorResponse;
import com.bloodbank.dto.StatisticsResponse;
import com.bloodbank.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/donors")
    public ResponseEntity<ApiResponse<List<DonorResponse>>> getAllDonors() {
        List<DonorResponse> donors = adminService.getAllDonors();
        return ResponseEntity.ok(ApiResponse.ok("All registered donors retrieved successfully", donors));
    }

    @GetMapping("/donors/count")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getTotalDonorCount() {
        long count = adminService.getTotalDonorCount();
        return ResponseEntity.ok(ApiResponse.ok("Total donor count retrieved successfully", Map.of("count", count)));
    }

    @GetMapping("/donors/statistics")
    public ResponseEntity<ApiResponse<StatisticsResponse>> getStatistics() {
        StatisticsResponse stats = adminService.getStatistics();
        return ResponseEntity.ok(ApiResponse.ok("Blood bank statistics retrieved successfully", stats));
    }

    @DeleteMapping("/donors/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteDonor(@PathVariable String id) {
        adminService.deleteDonor(id);
        return ResponseEntity.ok(ApiResponse.ok("Donor record has been deleted successfully"));
    }
}

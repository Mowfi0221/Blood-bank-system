package com.bloodbank.service;

import com.bloodbank.dto.DonorResponse;
import com.bloodbank.dto.StatisticsResponse;
import com.bloodbank.exception.ResourceNotFoundException;
import com.bloodbank.model.Donor;
import com.bloodbank.repository.DonorRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final DonorRepository donorRepository;

    public AdminService(DonorRepository donorRepository) {
        this.donorRepository = donorRepository;
    }

    public List<DonorResponse> getAllDonors() {
        return donorRepository.findAll().stream()
                .map(DonorResponse::fromDonor)
                .collect(Collectors.toList());
    }

    public long getTotalDonorCount() {
        return donorRepository.count();
    }

    public StatisticsResponse getStatistics() {
        List<Donor> allDonors = donorRepository.findAll();

        long total = allDonors.size();
        long available = allDonors.stream().filter(d -> "YES".equalsIgnoreCase(d.getAvailability())).count();
        long unavailable = total - available;

        // Supported blood groups initialized with 0 count
        String[] supportedGroups = {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
        Map<String, Long> bloodGroupCounts = new LinkedHashMap<>();
        for (String bg : supportedGroups) {
            bloodGroupCounts.put(bg, 0L);
        }

        // Count per blood group
        for (Donor d : allDonors) {
            if (d.getBloodGroup() != null) {
                String bg = d.getBloodGroup().trim().toUpperCase();
                bloodGroupCounts.put(bg, bloodGroupCounts.getOrDefault(bg, 0L) + 1);
            }
        }

        // Count per city
        Map<String, Long> cityCounts = allDonors.stream()
                .filter(d -> d.getCity() != null && !d.getCity().trim().isEmpty())
                .collect(Collectors.groupingBy(d -> d.getCity().trim(), Collectors.counting()));

        return new StatisticsResponse(total, available, unavailable, bloodGroupCounts, cityCounts);
    }

    public void deleteDonor(String id) {
        if (!donorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Donor record with ID " + id + " not found");
        }
        donorRepository.deleteById(id);
    }
}

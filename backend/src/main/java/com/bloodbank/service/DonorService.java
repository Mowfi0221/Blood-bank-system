package com.bloodbank.service;

import com.bloodbank.dto.*;
import com.bloodbank.exception.BadRequestException;
import com.bloodbank.exception.ResourceNotFoundException;
import com.bloodbank.model.Donor;
import com.bloodbank.repository.DonorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DonorService {

    private final DonorRepository donorRepository;
    private final PasswordEncoder passwordEncoder;

    public DonorService(DonorRepository donorRepository, PasswordEncoder passwordEncoder) {
        this.donorRepository = donorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public DonorResponse register(DonorRegisterRequest req) {
        if (!req.getPassword().equals(req.getConfirmPassword())) {
            throw new BadRequestException("Password and Confirm Password do not match");
        }

        String phone = req.getPhone().trim();
        String email = req.getEmail().trim().toLowerCase();

        if (donorRepository.existsByPhone(phone)) {
            throw new BadRequestException("A donor with this phone number is already registered");
        }

        if (donorRepository.existsByEmail(email)) {
            throw new BadRequestException("A donor with this email address is already registered");
        }

        Donor donor = new Donor();
        donor.setName(req.getName().trim());
        donor.setAge(req.getAge());
        donor.setBloodGroup(req.getBloodGroup().trim().toUpperCase());
        donor.setCity(req.getCity().trim());
        donor.setPhone(phone);
        donor.setEmail(email);
        donor.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        donor.setAvailability("YES"); // Default YES
        donor.setCreatedAt(LocalDateTime.now());
        donor.setUpdatedAt(LocalDateTime.now());

        Donor saved = donorRepository.save(donor);
        return DonorResponse.fromDonor(saved);
    }

    public DonorResponse getMe(String phone) {
        Donor donor = donorRepository.findByPhone(phone)
                .orElseThrow(() -> new ResourceNotFoundException("Donor profile not found"));
        return DonorResponse.fromDonor(donor);
    }

    public DonorResponse updateMe(String currentPhone, DonorUpdateRequest req) {
        Donor donor = donorRepository.findByPhone(currentPhone)
                .orElseThrow(() -> new ResourceNotFoundException("Donor profile not found"));

        String newEmail = req.getEmail().trim().toLowerCase();
        if (!newEmail.equalsIgnoreCase(donor.getEmail()) && donorRepository.existsByEmail(newEmail)) {
            throw new BadRequestException("This email address is already in use by another donor");
        }

        donor.setName(req.getName().trim());
        donor.setAge(req.getAge());
        donor.setBloodGroup(req.getBloodGroup().trim().toUpperCase());
        donor.setCity(req.getCity().trim());
        donor.setEmail(newEmail);
        donor.setUpdatedAt(LocalDateTime.now());

        Donor updated = donorRepository.save(donor);
        return DonorResponse.fromDonor(updated);
    }

    public DonorResponse updateAvailability(String currentPhone, AvailabilityUpdateRequest req) {
        Donor donor = donorRepository.findByPhone(currentPhone)
                .orElseThrow(() -> new ResourceNotFoundException("Donor profile not found"));

        donor.setAvailability(req.getAvailability().trim().toUpperCase());
        donor.setUpdatedAt(LocalDateTime.now());

        Donor updated = donorRepository.save(donor);
        return DonorResponse.fromDonor(updated);
    }

    public List<DonorPublicResponse> searchPublicDonors(String bloodGroup, String city) {
        List<Donor> donors;
        boolean hasBloodGroup = StringUtils.hasText(bloodGroup) && !bloodGroup.equalsIgnoreCase("ALL");
        boolean hasCity = StringUtils.hasText(city) && !city.trim().isEmpty();

        if (hasBloodGroup && hasCity) {
            donors = donorRepository.findAvailableByBloodGroupAndCity(bloodGroup.trim().toUpperCase(), city.trim());
        } else if (hasBloodGroup) {
            donors = donorRepository.findAvailableByBloodGroup(bloodGroup.trim().toUpperCase());
        } else if (hasCity) {
            donors = donorRepository.findAvailableByCity(city.trim());
        } else {
            donors = donorRepository.findAllAvailable();
        }

        return donors.stream()
                .map(DonorPublicResponse::fromDonor)
                .collect(Collectors.toList());
    }
}

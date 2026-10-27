package com.bloodbank.security;

import com.bloodbank.model.Admin;
import com.bloodbank.model.Donor;
import com.bloodbank.repository.AdminRepository;
import com.bloodbank.repository.DonorRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final DonorRepository donorRepository;
    private final AdminRepository adminRepository;

    public CustomUserDetailsService(DonorRepository donorRepository, AdminRepository adminRepository) {
        this.donorRepository = donorRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // First check donor by phone
        Optional<Donor> donorOpt = donorRepository.findByPhone(username);
        if (donorOpt.isPresent()) {
            Donor donor = donorOpt.get();
            return new CustomUserDetails(donor.getId(), donor.getPhone(), donor.getPasswordHash(), "ROLE_DONOR");
        }

        // Then check admin by username
        Optional<Admin> adminOpt = adminRepository.findByUsername(username);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            return new CustomUserDetails(admin.getId(), admin.getUsername(), admin.getPasswordHash(), "ROLE_ADMIN");
        }

        throw new UsernameNotFoundException("User not found with identifier: " + username);
    }
}

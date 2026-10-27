package com.bloodbank.config;

import com.bloodbank.model.Admin;
import com.bloodbank.model.Donor;
import com.bloodbank.repository.AdminRepository;
import com.bloodbank.repository.DonorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final AdminRepository adminRepository;
    private final DonorRepository donorRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.default-username:admin}")
    private String defaultAdminUsername;

    @Value("${app.admin.default-password:admin123}")
    private String defaultAdminPassword;

    public DataInitializer(AdminRepository adminRepository,
                           DonorRepository donorRepository,
                           PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.donorRepository = donorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedAdmin();
        seedSampleDonors();
    }

    private void seedAdmin() {
        if (!adminRepository.existsByUsername(defaultAdminUsername)) {
            Admin admin = new Admin(defaultAdminUsername, passwordEncoder.encode(defaultAdminPassword));
            adminRepository.save(admin);
            logger.info(">>> Initialized default Administrator account: [username: {}]", defaultAdminUsername);
        }
    }

    private void seedSampleDonors() {
        if (donorRepository.count() == 0) {
            logger.info(">>> Seeding initial sample blood donor records into MongoDB...");

            createDonor("Rahul Sharma", 28, "O+", "Chennai", "9876543210", "rahul.sharma@example.com", "YES");
            createDonor("Priya Venkatesh", 24, "A+", "Chennai", "9876543211", "priya.v@example.com", "YES");
            createDonor("Ananya Raman", 22, "B+", "Bangalore", "9876543212", "ananya.raman@example.com", "YES");
            createDonor("Karthik Subramanian", 31, "AB+", "Coimbatore", "9876543213", "karthik.sub@example.com", "YES");
            createDonor("Deepak Kumar", 29, "O-", "Chennai", "9876543214", "deepak.k@example.com", "YES");
            createDonor("Sneha Patel", 26, "A-", "Mumbai", "9876543215", "sneha.patel@example.com", "YES");
            createDonor("Vikramaditya Rao", 35, "B-", "Hyderabad", "9876543216", "vikram.rao@example.com", "NO");
            createDonor("Meera Nambiar", 27, "AB-", "Bangalore", "9876543217", "meera.nambiar@example.com", "YES");

            logger.info(">>> Successfully seeded 8 initial donor records covering all blood groups!");
        }
    }

    private void createDonor(String name, int age, String bloodGroup, String city, String phone, String email, String availability) {
        Donor donor = new Donor();
        donor.setName(name);
        donor.setAge(age);
        donor.setBloodGroup(bloodGroup);
        donor.setCity(city);
        donor.setPhone(phone);
        donor.setEmail(email);
        donor.setPasswordHash(passwordEncoder.encode("password123"));
        donor.setAvailability(availability);
        donor.setCreatedAt(LocalDateTime.now().minusDays((long) (Math.random() * 30)));
        donor.setUpdatedAt(LocalDateTime.now());
        donorRepository.save(donor);
    }
}

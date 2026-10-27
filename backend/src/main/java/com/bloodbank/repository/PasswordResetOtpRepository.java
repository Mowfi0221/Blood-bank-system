package com.bloodbank.repository;

import com.bloodbank.model.PasswordResetOtp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetOtpRepository extends MongoRepository<PasswordResetOtp, String> {

    Optional<PasswordResetOtp> findTopByEmailOrderByCreatedAtDesc(String email);

    Optional<PasswordResetOtp> findTopByPhoneOrderByCreatedAtDesc(String phone);

    Optional<PasswordResetOtp> findTopByEmailOrPhoneOrderByCreatedAtDesc(String email, String phone);

    void deleteAllByEmail(String email);

    void deleteAllByPhone(String phone);
}

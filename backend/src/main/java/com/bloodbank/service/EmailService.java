package com.bloodbank.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public boolean sendOtpEmail(String toEmail, String otp) {
        if (!StringUtils.hasText(fromEmail) || fromEmail.contains("example") || fromEmail.trim().isEmpty()) {
            logger.info("SMTP credentials not configured or in development mode. Simulating email dispatch to: {}", toEmail);
            logger.info("=== [DEV MODE EMAIL SIMULATION] OTP for {} is: {} (Valid for 5 minutes) ===", toEmail, otp);
            return true;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Blood Bank Management System - Password Reset OTP");
            message.setText("Dear Donor,\n\n" +
                    "Your One-Time Password (OTP) for resetting your Blood Bank account password is: " + otp + "\n\n" +
                    "This OTP is valid for 5 minutes only. If you did not request this password reset, please ignore this email.\n\n" +
                    "Warm regards,\nBlood Bank Management System Team\nEvery Drop Counts. Every Donor Matters.");

            mailSender.send(message);
            logger.info("OTP email successfully dispatched to registered email address.");
            return true;
        } catch (Exception e) {
            logger.error("Failed to send email via SMTP: {}. Falling back to simulation mode.", e.getMessage());
            logger.info("=== [DEV FALLBACK] OTP for {} is: {} (Valid for 5 minutes) ===", toEmail, otp);
            return true;
        }
    }
}

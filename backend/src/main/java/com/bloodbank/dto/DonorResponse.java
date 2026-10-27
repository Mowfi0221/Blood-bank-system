package com.bloodbank.dto;

import com.bloodbank.model.Donor;

import java.time.LocalDateTime;

public class DonorResponse {

    private String id;
    private String name;
    private Integer age;
    private String bloodGroup;
    private String city;
    private String phone;
    private String email;
    private String availability;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public DonorResponse() {
    }

    public static DonorResponse fromDonor(Donor donor) {
        if (donor == null) return null;
        DonorResponse dto = new DonorResponse();
        dto.setId(donor.getId());
        dto.setName(donor.getName());
        dto.setAge(donor.getAge());
        dto.setBloodGroup(donor.getBloodGroup());
        dto.setCity(donor.getCity());
        dto.setPhone(donor.getPhone());
        dto.setEmail(donor.getEmail());
        dto.setAvailability(donor.getAvailability());
        dto.setCreatedAt(donor.getCreatedAt());
        dto.setUpdatedAt(donor.getUpdatedAt());
        return dto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

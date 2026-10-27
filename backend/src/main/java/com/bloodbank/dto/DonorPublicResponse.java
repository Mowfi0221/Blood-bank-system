package com.bloodbank.dto;

import com.bloodbank.model.Donor;

public class DonorPublicResponse {

    private String id;
    private String name;
    private String bloodGroup;
    private String city;
    private String phone;
    private String availability;

    public DonorPublicResponse() {
    }

    public static DonorPublicResponse fromDonor(Donor donor) {
        if (donor == null) return null;
        DonorPublicResponse dto = new DonorPublicResponse();
        dto.setId(donor.getId());
        dto.setName(donor.getName());
        dto.setBloodGroup(donor.getBloodGroup());
        dto.setCity(donor.getCity());
        dto.setPhone(donor.getPhone());
        dto.setAvailability(donor.getAvailability());
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

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}

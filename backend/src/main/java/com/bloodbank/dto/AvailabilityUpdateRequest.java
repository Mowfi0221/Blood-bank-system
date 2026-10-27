package com.bloodbank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class AvailabilityUpdateRequest {

    @NotBlank(message = "Availability status is required")
    @Pattern(regexp = "^(YES|NO)$", message = "Availability must be either YES or NO")
    private String availability;

    public AvailabilityUpdateRequest() {
    }

    public AvailabilityUpdateRequest(String availability) {
        this.availability = availability;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}

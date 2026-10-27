package com.bloodbank.dto;

import java.util.Map;

public class StatisticsResponse {

    private long totalDonors;
    private long availableDonors;
    private long unavailableDonors;
    private Map<String, Long> bloodGroupCounts;
    private Map<String, Long> cityCounts;

    public StatisticsResponse() {
    }

    public StatisticsResponse(long totalDonors, long availableDonors, long unavailableDonors,
                              Map<String, Long> bloodGroupCounts, Map<String, Long> cityCounts) {
        this.totalDonors = totalDonors;
        this.availableDonors = availableDonors;
        this.unavailableDonors = unavailableDonors;
        this.bloodGroupCounts = bloodGroupCounts;
        this.cityCounts = cityCounts;
    }

    public long getTotalDonors() {
        return totalDonors;
    }

    public void setTotalDonors(long totalDonors) {
        this.totalDonors = totalDonors;
    }

    public long getAvailableDonors() {
        return availableDonors;
    }

    public void setAvailableDonors(long availableDonors) {
        this.availableDonors = availableDonors;
    }

    public long getUnavailableDonors() {
        return unavailableDonors;
    }

    public void setUnavailableDonors(long unavailableDonors) {
        this.unavailableDonors = unavailableDonors;
    }

    public Map<String, Long> getBloodGroupCounts() {
        return bloodGroupCounts;
    }

    public void setBloodGroupCounts(Map<String, Long> bloodGroupCounts) {
        this.bloodGroupCounts = bloodGroupCounts;
    }

    public Map<String, Long> getCityCounts() {
        return cityCounts;
    }

    public void setCityCounts(Map<String, Long> cityCounts) {
        this.cityCounts = cityCounts;
    }
}

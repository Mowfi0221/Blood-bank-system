package com.bloodbank.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "donors")
public class Donor {

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("age")
    private Integer age;

    @Field("blood_group")
    private String bloodGroup;

    @Field("city")
    private String city;

    @Indexed(unique = true)
    @Field("phone")
    private String phone;

    @Indexed(unique = true)
    @Field("email")
    private String email;

    @Field("password_hash")
    private String passwordHash;

    @Field("availability")
    private String availability = "YES"; // Default YES

    @Field("created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Field("updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Donor() {
    }

    public Donor(String name, Integer age, String bloodGroup, String city, String phone, String email, String passwordHash, String availability) {
        this.name = name;
        this.age = age;
        this.bloodGroup = bloodGroup;
        this.city = city;
        this.phone = phone;
        this.email = email;
        this.passwordHash = passwordHash;
        this.availability = availability != null ? availability : "YES";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
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

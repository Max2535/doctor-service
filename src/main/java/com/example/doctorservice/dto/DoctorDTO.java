package com.example.doctorservice.dto;

import java.time.LocalDate;

public class DoctorDTO {
    private Long id;
    private String fullName;
    private String specialization;
    private String email;
    private String phone;
    private LocalDate joinedDate;
    private boolean active;

    // Constructors
    public DoctorDTO() {}

    public DoctorDTO(Long id, String fullName, String specialization) {
        this.id = id;
        this.fullName = fullName;
        this.specialization = specialization;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDate getJoinedDate() { return joinedDate; }
    public void setJoinedDate(LocalDate joinedDate) {
        this.joinedDate = joinedDate;
    }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
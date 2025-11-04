package com.hms.hospital_management_system.dto;

import com.hms.hospital_management_system.models.Doctor;
import com.hms.hospital_management_system.models.User;

public class RegisterDoctorRequest {
    private User user;
    private Doctor doctor;

    // Getters and setters
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }
}

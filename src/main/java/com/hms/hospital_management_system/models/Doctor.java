package com.hms.hospital_management_system.models;

import jakarta.persistence.*;

@Entity
@Table(name = "doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctor_id;

    @Column(length = 100 ,nullable = false)
    private String name;

    private String specialization;

    @Column(length = 100, unique = true, nullable = false)
    private String username;

    @Column(length = 255, nullable = false)
    private String hashedpassword;

    @Column(length = 100, unique = true, nullable = false)
    private String email;
    @Column(length = 15, unique = true, nullable = false)
    private String phone_number;
    
    private Boolean active;


    public Doctor() {
    }

    public Doctor(String name, String specialization, String username, String hashedpassword, String email, String phone_number, Boolean active) {
        this.name = name;
        this.specialization = specialization;
        this.username = username;
        this.hashedpassword = hashedpassword;
        this.email = email;
        this.phone_number = phone_number;
        this.active = active;
    }

    // setters and getters
    public Long getDoctor_id() {
        return doctor_id;
    }
    public void setDoctor_id(Long doctor_id) {
        this.doctor_id = doctor_id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSpecialization() {
        return specialization;
    }
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getHashedpassword() {
        return hashedpassword;
    }
    public void setHashedpassword(String hashedpassword) {
        this.hashedpassword = hashedpassword;
    }


}

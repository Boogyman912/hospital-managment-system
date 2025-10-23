package com.hms.hospital_management_system.models;

import jakarta.persistence.*;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Long doctor_id;//doctorId

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "email", length = 100, unique = true, nullable = false)
    private String email;

    @Column(name = "phone_number", length = 15, unique = true, nullable = false)
    private String phone_number;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "rating")
    private Double rating; // Average rating from feedbacks



    public Doctor() {
    }

    public Doctor(String name, String specialization, String email, String phone_number, Boolean active) {
        this.name = name;
        this.specialization = specialization;
        this.email = email;
        this.phone_number = phone_number;
        this.active = active;
        this.rating = 0.0; // Initial rating
    }

    // setters and getters
    public Long getDoctor_id() {
        return doctor_id;//doctoId
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
 
    public String getEmail() {
    return email;
}
public void setEmail(String email) {
    this.email = email;
}

public String getPhone_number() {
    return phone_number;
}
public void setPhone_number(String phone_number) {
    this.phone_number = phone_number;
}

public Boolean getActive() {
    return active;
}
public void setActive(Boolean active) {
    this.active = active;
}

public Double getRating() {
    return rating;  
}
public void setRating(Double rating) {
    this.rating = rating;
}


}

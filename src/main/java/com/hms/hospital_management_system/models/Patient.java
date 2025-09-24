package com.hms.hospital_management_system.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import com.hms.hospital_management_system.models.EmergencyContact;



@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 15)
    private String phoneNumber;

    @Column
    private String email;

    @Column(nullable = false, length = 10)
    private String sex;

    @Column
    private LocalDate dob;

    @Transient // Age can be calculated from dob
    private Integer age;

    @ManyToOne
    @JoinColumn(name = "emergency_contact_id")
    private EmergencyContact emergencyContact;

    public Patient() {
    }

    public Patient(String name, String phoneNumber, String email, String sex, LocalDate dob, EmergencyContact emergencyContact) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.sex = sex;
        this.dob = dob;
        this.emergencyContact = emergencyContact;
        this.age = calculateAge();
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
        this.age = calculateAge();
    }

    public Integer getAge() {
        return calculateAge();
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public EmergencyContact getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(EmergencyContact emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    private Integer calculateAge() {
        if (dob == null) return null;
        return LocalDate.now().getYear() - dob.getYear();
    }
}

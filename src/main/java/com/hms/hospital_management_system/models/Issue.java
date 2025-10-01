package com.hms.hospital_management_system.models;

import jakarta.persistence.*;
import java.time.LocalDateTime; 

@Entity
@Table(name = "issues")
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long issueId;

    @ManyToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor; // CHANGED: Use Doctor instead of Staff

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    private IssueStatus status;

    public enum IssueStatus {
        PENDING, RESOLVED
    }

    public Issue() {
    }

    public Issue(Appointment appointment, Doctor doctor, String description, IssueStatus status) { // CHANGED: Doctor instead of Staff
        this.appointment = appointment;
        this.doctor = doctor;
        this.description = description;
        this.status = status;
    }

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Doctor getDoctor() { // CHANGED: return Doctor
        return doctor;
    }

    public void setDoctor(Doctor doctor) { // CHANGED: accept Doctor
        this.doctor = doctor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IssueStatus getStatus() {
        return status;
    }

    public void setStatus(IssueStatus status) {
        this.status = status;
    }
}
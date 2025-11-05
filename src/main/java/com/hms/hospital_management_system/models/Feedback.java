package com.hms.hospital_management_system.models;

import jakarta.persistence.*;
import java.time.LocalDateTime; 


@Entity
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackId;

    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    private Double rating; // 1-5

    @Column(length = 500)
    private String comments;

    private LocalDateTime submittedAt;
    public Feedback() {
        this.submittedAt = LocalDateTime.now();
    }

    
    public Feedback(Long feedbackId, Appointment appointment, Patient patient, Double rating, String comments) {
        this.feedbackId = feedbackId;
        this.appointment = appointment;
        this.patient = patient;
        this.rating = rating;
        this.comments = comments;
        this.submittedAt = LocalDateTime.now();
    }

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

}

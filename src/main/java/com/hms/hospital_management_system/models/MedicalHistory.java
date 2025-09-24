package com.hms.hospital_management_system.models;

import jakarta.persistence.*;
import java.time.LocalDateTime; 
import com.hms.hospital_management_system.models.Patient;
import com.hms.hospital_management_system.models.Doctor;

@Entity
@Table(name = "medical_histories")
public class MedicalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;    

    @Column(length = 500)
    private String diagnosis;

    @Column(length = 500)
    private String treatment;

    @Column(length = 200)
    private String allergies;

    @Column(length = 500)
    private String notes;

    private LocalDateTime lastUpdated;
    public MedicalHistory() {
    }

    public MedicalHistory(Patient patient, String diagnosis, String treatment, String allergies, String notes, LocalDateTime lastUpdated) {
        this.patient = patient;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.allergies = allergies;
        this.notes = notes;
        this.lastUpdated = lastUpdated;
    }

    public Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}

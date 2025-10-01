package com.hms.hospital_management_system.models;
import jakarta.persistence.*;
import java.time.LocalDateTime; 
import java.time.LocalDate;
import java.util.*;
@Entity
@Table(name = "prescriptions")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prescriptionId;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor; // CHANGED: Use Doctor instead of Staff

    //why is columnDefinition = "json"?
    //because we want to store the medications in a json format
    //the json format will be like this:
    //{
    //    "medication_name": "medication_name",
    //     "medication_brand": "medication_brand",
    //    "medication_quantity": "medication_quantity",
    //    "medication_instructions": "medication_instructions"
    //}
    //pricing will be considered in the inventory table and billing table
    @Column(columnDefinition = "json")
    private List<String> medications; // JSON string
    //Tests should be provided in this format 
    //{
    //    "test_name": "test_name",
    //     "test_type": "test_type",
    //}

    @Column(columnDefinition = "json")
    private List<String> labTests;
    // it will be a list of lab tests in json format

    @Column(length = 500)
    private String instructions;

    private LocalDate dateIssued;

    public Prescription() {
    }

    public Prescription(Appointment appointment, Patient patient, Doctor doctor, List<String> medications, List<String> labTests, String instructions, LocalDate dateIssued) {
        this.appointment = appointment;
        this.patient = patient;
        this.doctor = doctor;
        this.medications = medications;
        this.labTests = labTests;
        this.instructions = instructions;
        this.dateIssued = dateIssued;
    }

    public Long getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Long prescriptionId) {
        this.prescriptionId = prescriptionId;
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

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public List<String> getMedications() {
        return medications;
    }

    public void setMedications(List<String> medications) {
        this.medications = medications;
    }

    public List<String> getLabTests() {
        return labTests;
    }

    public void setLabTests(List<String> labTests) {
        this.labTests = labTests;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public LocalDate getDateIssued() {
        return dateIssued;
    }

    public void setDateIssued(LocalDate dateIssued) {
        this.dateIssued = dateIssued;
    }

}
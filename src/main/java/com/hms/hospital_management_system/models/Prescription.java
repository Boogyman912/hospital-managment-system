package com.hms.hospital_management_system.models;
import jakarta.persistence.*;
import java.time.LocalDateTime; 
import java.time.LocalDate;
import java.util.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hms.hospital_management_system.converter.ListMapToJsonConverter;
@Entity
@Table(name = "prescriptions")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prescriptionId;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor; // CHANGED: Use Doctor instead of Staff

    //why is columnDefinition = "json"?
    //because we want to store the medications in a json format
    //the json format will be like this:
    //{
    //    "itemName": "medication_name",
    //    "brandName": "medication_brand",
    //    "quantity": "medication_quantity",
    //    "medication_instructions": "medication_instructions"
    //}
    //pricing will be considered in the inventory table and billing table
    @Column(columnDefinition = "json")
    @Convert(converter = ListMapToJsonConverter.class)
    private List<Map<String,String>> medications; // JSON string
    //Tests should be provided in this format 
    //{
    //    "testName": "test_name",
    //     "testType": "test_type",
    //}

    @Column(columnDefinition = "json")
    @Convert(converter = ListMapToJsonConverter.class)
    private List<Map<String,String>> labTests;
    // it will be a list of lab tests in json format

    @Column(length = 500)
    private String instructions;

    private LocalDate dateIssued;

    public Prescription() {
    }

    public Prescription(Appointment appointment, Patient patient, Doctor doctor, List<Map<String,String>> medications, List<Map<String,String>> labTests, String instructions) {
        this.appointment = appointment;
        this.patient = patient;
        this.doctor = doctor;
        this.medications = medications;
        this.labTests = labTests;
        this.instructions = instructions;
        this.dateIssued = LocalDate.now();
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

    public List<Map<String,String>> getMedications() {
        return medications;
    }

    public void setMedications(List<Map<String,String>> medications) {
        this.medications = medications;
    }

    public List<Map<String,String>> getLabTests() {
        return labTests;
    }

    public void setLabTests(List<Map<String,String>> labTests) {
        this.labTests = labTests;
    }

    public void addMedications(List<Map<String,String>> newMedications) {
        if (this.medications == null) {
            this.medications = new ArrayList<>();
        }
        this.medications.addAll(newMedications);
    }

    public void addLabTests(List<Map<String,String>> newLabTests) {
        if (this.labTests == null) {
            this.labTests = new ArrayList<>();
        }
        this.labTests.addAll(newLabTests);
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
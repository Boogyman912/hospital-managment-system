package com.hms.hospital_management_system.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "lab_tests")
public class LabTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testId;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    private String testType;

    private LocalDate dateBooked;

    private String resultUrl;

    @Enumerated(EnumType.STRING)
    private LabTestStatus status;

    public enum LabTestStatus { PENDING, COMPLETED, CANCELLED }

    public LabTest() {
    }

    public LabTest(Patient patient, String testType, LocalDate dateBooked, String resultUrl, LabTestStatus status) {
        this.patient = patient;
        this.testType = testType;
        this.dateBooked = dateBooked;
        this.resultUrl = resultUrl;
        this.status = status;
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public LocalDate getDateBooked() {
        return dateBooked;
    }

    public void setDateBooked(LocalDate dateBooked) {
        this.dateBooked = dateBooked;
    }

    public String getResultUrl() {
        return resultUrl;
    }

    public void setResultUrl(String resultUrl) {
        this.resultUrl = resultUrl;
    }

    public LabTestStatus getStatus() {
        return status;
    }

    public void setStatus(LabTestStatus status) {
        this.status = status;
    }
}

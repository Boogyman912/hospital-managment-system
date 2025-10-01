package com.hms.hospital_management_system.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.hms.hospital_management_system.models.Appointment;

@Entity
@Table(name = "lab_tests")
public class LabTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testId;

    // @ManyToOne
    // @JoinColumn(name = "appointment_id")
    // private Appointment  appointment;
    // @ManyToOne
    // @JoinColumn(name = "patient_id")
    // private Patient patient;

    private String testName;

    private String testType; // e.g., Blood Test, X-Ray, MRI

    private Double testCost;

    public LabTest() {
    }

    public LabTest(Long testId, String testName, String testType, Double testCost) {
        this.testId = testId;
        this.testName = testName;
        this.testType = testType;
        this.testCost = testCost;
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public Double getTestCost() {
        return testCost;
    }

    public void setTestCost(Double testCost) {
        this.testCost = testCost;
    }

}

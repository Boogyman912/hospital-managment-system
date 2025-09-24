package com.hms.hospital_management_system.models;

import jakarta.persistence.*;
import java.time.LocalDateTime; 

import com.hms.hospital_management_system.models.Patient;

@Entity
@Table(name = "insurance")
public class Insurance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long insuranceId;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    private String provider;

    private String policyNumber;

    @Column(length = 500)
    private String coverageDetails;

    private String claimStatus;

    public Insurance() {
    }

    public Insurance(Long insuranceId, Patient patient, String provider, String policyNumber, String coverageDetails, String claimStatus) {
        this.insuranceId = insuranceId;
        this.patient = patient;
        this.provider = provider;
        this.policyNumber = policyNumber;
        this.coverageDetails = coverageDetails;
        this.claimStatus = claimStatus;
    }

    public Long getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(Long insuranceId) {
        this.insuranceId = insuranceId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getCoverageDetails() {
        return coverageDetails;
    }

    public void setCoverageDetails(String coverageDetails) {
        this.coverageDetails = coverageDetails;
    }

    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }
}

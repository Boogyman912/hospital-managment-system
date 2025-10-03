package com.hms.hospital_management_system.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalDate; 
import java.util.List;
import com.hms.hospital_management_system.models.Prescription;
@Entity
@Table(name = "billing")
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billingId;

    @OneToOne
    @JoinColumn(name = "prescription_id")
    @Column(nullable = true)
    private Prescription prescription;

    private Patient patient;
    @OneToMany
    @JoinColumn(name = "inpatient_id")
    @Column(nullable = true)
    private List<Inpatient> inpatients;
    private Double totalAmount;
    
    private Status status;
    public enum Status {
        PAID, UNPAID, PENDING
    }

    private LocalDate paymentDate;

    public Billing() {
    }

    public Billing(Prescription prescription, List<Inpatient> inpatients, Patient patient , Double totalAmount, LocalDate paymentDate, Status status) {
        this.prescription = prescription;
        this.inpatients = inpatients;
        this.totalAmount = totalAmount;
        this.paymentDate = paymentDate;
        this.status = status;
        this.patient = patient;
    }
    public List<Inpatient> getInpatients() {
        return inpatients;
    }
    public void setInpatients(List<Inpatient> inpatients) {
        this.inpatients = inpatients;
    }

    public Patient getPatient() {
        return patient;
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Long getBillingId() {
        return billingId;
    }

    public void setBillingId(Long billingId) {
        this.billingId = billingId;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }
}

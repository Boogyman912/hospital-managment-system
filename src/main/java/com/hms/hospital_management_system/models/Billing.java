package com.hms.hospital_management_system.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalDate; 

@Entity
@Table(name = "billing")
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billingId;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    private Double totalAmount;

    private Double amountPaid;

    private Double dueAmount;

    private LocalDate paymentDate;

    public Billing() {
    }

    public Billing(Patient patient, Double totalAmount, Double amountPaid, Double dueAmount, LocalDate paymentDate) {
        this.patient = patient;
        this.totalAmount = totalAmount;
        this.amountPaid = amountPaid;
        this.dueAmount = dueAmount;
        this.paymentDate = paymentDate;
    }

    public Long getBillingId() {
        return billingId;
    }

    public void setBillingId(Long billingId) {
        this.billingId = billingId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Double getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(Double dueAmount) {
        this.dueAmount = dueAmount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }
}

package com.hms.hospital_management_system.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "receipts")
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long receiptId;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    private Double amount;

    private String paymentMethod;

    private LocalDateTime timestamp;

    private String pdfUrl;
    public Receipt() {
    }

    public Receipt(Appointment appointment, Double amount, String paymentMethod, LocalDateTime timestamp, String pdfUrl) {
        this.appointment = appointment;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.timestamp = timestamp;
        this.pdfUrl = pdfUrl;
    }

    public Long getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }
}

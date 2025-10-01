package com.hms.hospital_management_system.models;
import jakarta.persistence.*;
import java.time.LocalDate;
@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "slot_id")
    private Slot slot;

    private LocalDate bookingTime;

    @Column(nullable = false)
    private String paymentStatus;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus appointmentStatus;

    private Boolean isOnline;

    private String meetLink;

    @ManyToOne
    @JoinColumn(name = "receipt_id")
    private Receipt receipt;

    public enum AppointmentStatus {
        BOOKED, COMPLETED, CANCELLED, ISSUE_RAISED
    }

public Appointment() {
}

public Appointment(Long appointmentId, Doctor doctor, Patient patient, Slot slot, LocalDate bookingTime,
                   String paymentStatus, AppointmentStatus appointmentStatus, Boolean isOnline,
                   String meetLink, Receipt receipt) {
    this.appointmentId = appointmentId;
    this.doctor = doctor;
    this.patient = patient;
    this.slot = slot;
    this.bookingTime = bookingTime;
    this.paymentStatus = paymentStatus;
    this.appointmentStatus = appointmentStatus;
    this.isOnline = isOnline;
    this.meetLink = meetLink;
    this.receipt = receipt;
}

public Long getAppointmentId() {
    return appointmentId;
}

public void setAppointmentId(Long appointmentId) {
    this.appointmentId = appointmentId;
}

public Doctor getDoctor() {
    return doctor;
}

public void setDoctor(Doctor doctor) {
    this.doctor = doctor;
}

public Patient getPatient() {
    return patient;
}

public void setPatient(Patient patient) {
    this.patient = patient;
}

public Slot getSlot() {
    return slot;
}

public void setSlot(Slot slot) {
    this.slot = slot;
}

public LocalDate getBookingTime() {
    return bookingTime;
}

public void setBookingTime(LocalDate bookingTime) {
    this.bookingTime = bookingTime;
}

public String getPaymentStatus() {
    return paymentStatus;
}

public void setPaymentStatus(String paymentStatus) {
    this.paymentStatus = paymentStatus;
}

public AppointmentStatus getAppointmentStatus() {
    return appointmentStatus;
}

public void setAppointmentStatus(AppointmentStatus appointmentStatus) {
    this.appointmentStatus = appointmentStatus;
}

public Boolean getIsOnline() {
    return isOnline;
}

public void setIsOnline(Boolean isOnline) {
    this.isOnline = isOnline;
}

public String getMeetLink() {
    return meetLink;
}

public void setMeetLink(String meetLink) {
    this.meetLink = meetLink;
}

public Receipt getReceipt() {
    return receipt;
}

public void setReceipt(Receipt receipt) {
    this.receipt = receipt;
}
}

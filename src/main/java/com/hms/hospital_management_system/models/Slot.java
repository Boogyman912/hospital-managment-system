package com.hms.hospital_management_system.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "slots", indexes = {
    @Index(name = "idx_slot_doctor_date_status", columnList = "doctor_id, date, status"),
    @Index(name = "idx_slot_status", columnList = "status"),
    @Index(name = "idx_slot_date", columnList = "date")
})
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long slotId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    private LocalDate date;

    private String time;

    private Boolean isOnline;

    @Enumerated(EnumType.STRING)
    private SlotStatus status;

    private LocalDateTime heldUntil;

    public enum SlotStatus {
        AVAILABLE, BOOKED, HELD
    }

    public Slot() {
    }

    public Slot(Doctor doctor, LocalDate date, String time, Boolean isOnline, SlotStatus status, LocalDateTime heldUntil) {
        this.doctor = doctor;
        this.date = date;
        this.time = time;
        this.isOnline = isOnline;
        this.status = status;
        this.heldUntil = heldUntil;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }

    public SlotStatus getStatus() {
        return status;
    }

    public void setStatus(SlotStatus status) {
        this.status = status;
    }

    public LocalDateTime getHeldUntil() {
        return heldUntil;
    }

    public void setHeldUntil(LocalDateTime heldUntil) {
        this.heldUntil = heldUntil;
    }
}

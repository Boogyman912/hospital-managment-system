package com.hms.hospital_management_system.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.hms.hospital_management_system.models.Staff;

@Entity
@Table(name = "slots")
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long slotId;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Staff doctor;

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

    public Slot(Staff doctor, LocalDate date, String time, Boolean isOnline, SlotStatus status, LocalDateTime heldUntil) {
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

    public Staff getDoctor() {
        return doctor;
    }

    public void setDoctor(Staff doctor) {
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

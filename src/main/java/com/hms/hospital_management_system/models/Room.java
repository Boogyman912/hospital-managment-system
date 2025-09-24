package com.hms.hospital_management_system.models;

import jakarta.persistence.*;
import java.time.LocalDateTime; 
import com.hms.hospital_management_system.models.Patient;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    private String roomNumber;

    @Enumerated(EnumType.STRING)
    private RoomType type;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient; // nullable

    public enum RoomType { GENERAL, ICU, PRIVATE }
    public enum RoomStatus { AVAILABLE, OCCUPIED }
    public Room() {
        // Default constructor for JPA
    }

    public Room(String roomNumber, RoomType type, RoomStatus status, Patient patient) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.status = status;
        this.patient = patient;
    }
    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
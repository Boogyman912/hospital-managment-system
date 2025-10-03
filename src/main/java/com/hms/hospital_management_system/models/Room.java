package com.hms.hospital_management_system.models;

import jakarta.persistence.*;
import java.time.LocalDateTime; 

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

    private LocalDateTime lastUpdated;

    private Double pricePerDay;

    public enum RoomType { GENERAL, ICU, PRIVATE }
    public enum RoomStatus { AVAILABLE, OCCUPIED }
   
    private Double genRoom = 1000.0;
    private Double icuRoom = 5000.0;
    private Double privateRoom = 3000.0;

    private Double setPrice(RoomType type) {
        switch (type) {
            case GENERAL:
                return genRoom;
            case ICU:
                return icuRoom;
            case PRIVATE:
                return privateRoom;
            default:
                return 0.0;
        }
    }

    public Room(){
    }

    public Room(String roomNumber, RoomType type, RoomStatus status, LocalDateTime lastUpdated) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.status = status;
        this.lastUpdated = lastUpdated;
        this.pricePerDay = setPrice(type);
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
        this.pricePerDay = setPrice(type);
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(Double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }


    // Set base prices for room types
    public void setBasePrice(RoomType type, Double price) {
        switch(type) {
            case GENERAL:
                genRoom = price;
                break;
            case ICU:
                icuRoom = price;
                break;
            case PRIVATE:
                privateRoom = price;
                break;
            default:
                break;
        }
        // Update pricePerDay if type matches
        if (this.type == type) {
            this.pricePerDay = price;
        }
    }

    // Get base price for a room type
    public Double getBasePrice(RoomType type) {
        switch(type) {
            case GENERAL:
                return genRoom;
            case ICU:
                return icuRoom;
            case PRIVATE:
                return privateRoom;
            default:
                return 0.0;
        }
    }


}
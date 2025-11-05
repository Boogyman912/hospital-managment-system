package com.hms.hospital_management_system.services;

import org.springframework.stereotype.Service;
// import RoomStatus from Room model
import com.hms.hospital_management_system.models.Room.*;

import java.util.*;
import com.hms.hospital_management_system.models.Room;
import com.hms.hospital_management_system.jpaRepository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    public Room updateRoom(Long id, Room room) {
        return roomRepository.save(room);
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    public List<Room> getRoomsByType(String type) {
        RoomType roomType = RoomType.valueOf(type);
        return roomRepository.findByType(roomType);
    }

    public List<Room> getRoomsByStatus(String status) {
        RoomStatus roomStatus = RoomStatus.valueOf(status);
        return roomRepository.findByStatus(roomStatus);
    }

    // find by status and type
    public List<Room> getRoomsByTypeAndStatus(String type, String status) {
        RoomType roomType = RoomType.valueOf(type);
        RoomStatus roomStatus = RoomStatus.valueOf(status);
        return roomRepository.findByTypeAndStatus(roomType, roomStatus);
    }

    // update room status
    public Room updateRoomStatus(Long id, String status) {
        Room room = roomRepository.findById(id).orElse(null);
        if (room != null) {
            RoomStatus status2 = RoomStatus.valueOf(status);
            room.setStatus(status2);
            return roomRepository.save(room);
        }
        return null;
    }
}

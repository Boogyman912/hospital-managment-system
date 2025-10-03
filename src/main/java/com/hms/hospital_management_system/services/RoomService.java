package com.hms.hospital_management_system.services;
import org.springframework.stereotype.Service;
//import RoomStatus from Room model
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
    
    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElse(null);
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
        return roomRepository.findByType(type);
    }
    
    public List<Room> getRoomsByStatus(String status) {
        return roomRepository.findByStatus(status);
    }

    // find by status and type
    public List<Room> getRoomsByTypeAndStatus(String type, String status) {
        return roomRepository.findByTypeAndStatus(type, status);
    }   

    //update room status
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

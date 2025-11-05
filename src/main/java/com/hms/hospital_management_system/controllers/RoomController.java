package com.hms.hospital_management_system.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import com.hms.hospital_management_system.services.RoomService;
import com.hms.hospital_management_system.models.Room;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/staff/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/all")
    public ResponseEntity<List<Room>> getAllRooms() {
        List<Room> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Room>> getRoomsByStatus(@PathVariable String status) {
        List<Room> rooms = roomService.getRoomsByStatus(status);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Room>> getRoomsByType(@PathVariable String type) {
        
        List<Room> rooms = roomService.getRoomsByType(type);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/type/{type}/status/{status}")
    public ResponseEntity<List<Room>> getRoomsByTypeAndStatus(@PathVariable String type,
            @PathVariable String status) {
        List<Room> rooms = roomService.getRoomsByTypeAndStatus(type, status);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        try {
            Optional<Room> optionalRoom = roomService.getRoomById(id);

            if (optionalRoom.isPresent()) {
                return ResponseEntity.ok(optionalRoom.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // or
                                                                               // ResponseEntity.notFound().build()
            }
        } catch (Exception e) {
            // Log the exception if needed (using a logger)
            System.err.println("Error while fetching room by ID: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    @PostMapping("/create")
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        Room createdRoom = roomService.createRoom(room);
        if (createdRoom != null) {
            return ResponseEntity.ok(createdRoom);
        } else {
            return ResponseEntity.status(500).build();
        }
    }

    @PatchMapping("/update/{id}/status/{status}")
    public ResponseEntity<Room> updateRoomStatus(@PathVariable Long id,
            @PathVariable String status) {
        Room updatedRoom = roomService.updateRoomStatus(id, status);
        if (updatedRoom != null) {
            return ResponseEntity.ok(updatedRoom);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @RequestBody Room room) {
        Room updatedRoom = roomService.updateRoom(id, room);
        if (updatedRoom != null) {
            return ResponseEntity.ok(updatedRoom);
        } else {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        try {
            roomService.deleteRoom(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }



}

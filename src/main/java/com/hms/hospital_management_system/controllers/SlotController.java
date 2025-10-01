package com.hms.hospital_management_system.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hms.hospital_management_system.models.Slot;
import com.hms.hospital_management_system.services.SlotService;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/slots")
public class SlotController {
    
    @Autowired
    private SlotService slotService;
    
    @GetMapping("/doctor/{doctor_id}/date/{date}")
    public ResponseEntity<List<Slot>> getAvailableSlots(
            @PathVariable Long doctor_id, 
            @PathVariable String date) {
        LocalDate appointmentDate = LocalDate.parse(date);
        List<Slot> slots = slotService.getAvailableSlotsByDoctorAndDate(doctor_id, appointmentDate);
        return ResponseEntity.ok(slots);
    }
    
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createSlots(@RequestBody Map<String, Object> slotRequest) {
        try {
            Long doctor_id = Long.valueOf(slotRequest.get("doctor_id").toString());
            String dateStr = slotRequest.get("date").toString();
            // CHANGED: Postman sends JSON arrays as List, not String[]. Convert safely.
            @SuppressWarnings("unchecked")
            List<String> timeSlotsList = (List<String>) slotRequest.get("timeSlots");
            if (timeSlotsList == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "timeSlots is required and must be an array of strings"
                ));
            }
            String[] timeSlots = timeSlotsList.toArray(new String[0]);
            Boolean isOnline = Boolean.valueOf(slotRequest.get("isOnline").toString());
            
            LocalDate date = LocalDate.parse(dateStr);
            // CHANGED: use return value to confirm how many were saved
            //print all time slots
            // System.out.println("Time slots: ");
            // for (String time : timeSlots) {
            //     System.out.println("Time slot: " + time);
            // }
            int saved = slotService.createSlotsForDoctor(doctor_id, date, timeSlots, isOnline);
            if (saved == 0) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "No slots were created. Check doctor_id, date, and timeSlots."
                ));
            }
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Slots created successfully",
                "count", saved
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Error creating slots: " + e.getMessage()
            ));
        }
    }
    
    @PostMapping("/{slotId}/hold")
    public ResponseEntity<Map<String, Object>> holdSlot(@PathVariable Long slotId, @RequestParam(defaultValue = "15") int holdMinutes) {
        try {
            Slot slot = slotService.holdSlot(slotId, holdMinutes);
            if (slot != null) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Slot held successfully",
                    "slotId", slot.getSlotId(),
                    "heldUntil", slot.getHeldUntil()
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Slot not available or already booked"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Error holding slot: " + e.getMessage()
            ));
        }
    }
    
    @PostMapping("/{slotId}/release")
    public ResponseEntity<Map<String, Object>> releaseSlot(@PathVariable Long slotId) {
        try {
            slotService.releaseSlot(slotId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Slot released successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Error releasing slot: " + e.getMessage()
            ));
        }
    }
}

package com.hms.hospital_management_system.services;

import com.hms.hospital_management_system.jpaRepository.SlotRepository;
import com.hms.hospital_management_system.models.Doctor;
import com.hms.hospital_management_system.models.Slot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SlotService {
    
    @Autowired
    private SlotRepository slotRepository;
    
    @Autowired
    private DoctorService doctorService;
    
    public List<Slot> getAvailableSlotsByDoctorAndDate(Long doctor_id, LocalDate date) {
        try {
            // CHANGED: repository method renamed to use explicit JPQL method name findByDoctorAndDateAndStatus
            return slotRepository.findByDoctorAndDateAndStatus(doctor_id, date, Slot.SlotStatus.AVAILABLE);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
    
    public Slot holdSlot(Long slotId, int holdMinutes) {
        try {
            Slot slot = slotRepository.findById(slotId).orElse(null);
            if (slot != null && slot.getStatus() == Slot.SlotStatus.AVAILABLE) {
                slot.setStatus(Slot.SlotStatus.HELD);
                slot.setHeldUntil(LocalDateTime.now().plusMinutes(holdMinutes));
                return slotRepository.save(slot);
            }
            return null;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
    
    public Slot bookSlot(Long slotId) {
        try {
            Slot slot = slotRepository.findById(slotId).orElse(null);
            if (slot != null && (slot.getStatus() == Slot.SlotStatus.AVAILABLE || slot.getStatus() == Slot.SlotStatus.HELD)) {
                slot.setStatus(Slot.SlotStatus.BOOKED);
                slot.setHeldUntil(null);
                return slotRepository.save(slot);
            }
            return null;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
    
    public void releaseSlot(Long slotId) {
        try {
            Slot slot = slotRepository.findById(slotId).orElse(null);
            if (slot != null && slot.getStatus() == Slot.SlotStatus.HELD) {
                slot.setStatus(Slot.SlotStatus.AVAILABLE);
                slot.setHeldUntil(null);
                slotRepository.save(slot);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    // CHANGED: Return number of slots actually saved; validate doctor and input list
    public int createSlotsForDoctor(Long doctor_id, LocalDate date, String[] timeSlots, boolean isOnline) {
        int saved = 0;
        try {
            Doctor doctor = doctorService.getDoctorById(doctor_id);
            if (doctor == null) {
                System.out.println("Error: Doctor not found for id=" + doctor_id);
                return 0;
            }
            if (timeSlots == null || timeSlots.length == 0) {
                System.out.println("Error: No timeSlots provided");
                return 0;
            }
            for (String time : timeSlots) {
                try {
                    Slot slot = new Slot();
                    slot.setDoctor(doctor);
                    slot.setDate(date);
                    slot.setTime(time);
                    slot.setIsOnline(isOnline);
                    slot.setStatus(Slot.SlotStatus.AVAILABLE);
                    slotRepository.save(slot);
                    saved++;
                } catch (Exception inner) {
                    // continue with other slots if one fails
                    System.out.println("Error saving slot for time " + time + ": " + inner.getMessage());
                }
            }
            return saved;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return saved;
        }
    }
}

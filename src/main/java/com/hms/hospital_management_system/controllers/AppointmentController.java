package com.hms.hospital_management_system.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hms.hospital_management_system.models.Appointment;
import com.hms.hospital_management_system.services.AppointmentService;
import com.hms.hospital_management_system.services.SlotService;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import com.hms.hospital_management_system.models.Slot;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    
    @Autowired
    private AppointmentService appointmentService;
    
    @Autowired
    private SlotService slotService;

   
    // @GetMapping("/doctor/{doctor_id}")
    // public ResponseEntity<List<Appointment>> getAppointmentsByDoctor(@PathVariable Long doctor_id) {
    //     List<Appointment> appointments = appointmentService.getAppointmentsByDoctorId(doctor_id);
    //     return ResponseEntity.ok(appointments);
    // }



    
    
    
    
    @GetMapping("/slots/doctor/{doctor_id}/date/{date}")
    public ResponseEntity<List<com.hms.hospital_management_system.models.Slot>> getAvailableSlots(
            @PathVariable Long doctor_id, 
            @PathVariable String date) {
        LocalDate appointmentDate = LocalDate.parse(date);
        List<Slot> slots = 
            slotService.getAvailableSlotsByDoctorAndDate(doctor_id, appointmentDate);
        return ResponseEntity.ok(slots);
    }
    
    @PostMapping("/book")
    public ResponseEntity<Map<String, Object>> bookAppointment(@RequestBody Map<String, Object> bookingRequest) {
        try {
            Long patientId = Long.valueOf(bookingRequest.get("patientId").toString());
            Long doctor_id = Long.valueOf(bookingRequest.get("doctor_id").toString());
            Long slotId = Long.valueOf(bookingRequest.get("slotId").toString());
            Boolean isOnline = Boolean.valueOf(bookingRequest.get("isOnline").toString());
            String meetLink = bookingRequest.get("meetLink") != null ? bookingRequest.get("meetLink").toString() : null;
            
            Appointment appointment = appointmentService.bookAppointment(patientId, doctor_id, slotId, isOnline, meetLink);
            
            if (appointment != null) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Appointment booked successfully",
                    "appointmentId", appointment.getAppointmentId(),
                    "paymentStatus", appointment.getPaymentStatus()
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Failed to book appointment"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Error booking appointment: " + e.getMessage()
            ));
        }
    }
    
    @PostMapping("/{appointmentId}/payment")
    public ResponseEntity<Map<String, Object>> confirmPayment(
            @PathVariable Long appointmentId,
            @RequestBody Map<String, Object> paymentRequest) {
        try {
            String paymentMethod = paymentRequest.get("paymentMethod").toString();
            Double amount = Double.valueOf(paymentRequest.get("amount").toString());
            
            Appointment appointment = appointmentService.confirmPayment(appointmentId, paymentMethod, amount);
            
            if (appointment != null) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Payment confirmed successfully",
                    "appointmentId", appointment.getAppointmentId(),
                    "paymentStatus", appointment.getPaymentStatus()
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Failed to confirm payment"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Error confirming payment: " + e.getMessage()
            ));
        }
    }
    
    @PostMapping("/cancel/appointment/{appointmentId}")
    public ResponseEntity<Map<String, Object>> cancelAppointment(@PathVariable Long appointmentId) {
        try {
            // can integrate a refund process
            appointmentService.cancelAppointment(appointmentId);
            return ResponseEntity
                    .ok(Map.of("success", true, "message", "Appointment cancelled successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message",
                    "Error cancelling appointment: " + e.getMessage()));
        }
    }
}

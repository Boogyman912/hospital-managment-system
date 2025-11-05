package com.hms.hospital_management_system.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hms.hospital_management_system.models.Appointment;
import com.hms.hospital_management_system.models.Appointment.AppointmentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import com.hms.hospital_management_system.services.AppointmentService;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patient")
public class PublicPatientController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/appointment/{phoneNumber}")
    public ResponseEntity<?> getAppointmentByPhoneNumber(@PathVariable String phoneNumber) {
        try {
            // Validate phone number format (basic validation)
            if (phoneNumber == null || phoneNumber.trim().isEmpty() || !phoneNumber.matches("\\d{10,15}")) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Invalid phone number format"
                ));
            }

            // Fetch appointments from the service
            List<Appointment> appointments = appointmentService.findByPhoneNumber(phoneNumber);

            // Filter only the ones that are not completed (status = BOOKED)
            List<Appointment> incompleteAppointments = appointments.stream()
                .filter(appointment -> appointment.getAppointmentStatus() == AppointmentStatus.BOOKED)
                .collect(Collectors.toList());

            // If no appointments found, return 404 response
            if (incompleteAppointments.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", "No appointments found"
                ));
            }

            // Return the filtered list
            return ResponseEntity.ok(Map.of(
                "success", true,
                "appointments", incompleteAppointments
            ));

        } catch (Exception e) {
            // Handle any unexpected errors - don't expose internal details
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Error retrieving appointments. Please try again later."
            ));
        }
    }
}

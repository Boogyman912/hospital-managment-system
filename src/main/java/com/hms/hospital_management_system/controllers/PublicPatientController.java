package com.hms.hospital_management_system.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hms.hospital_management_system.models.Appointment;
import com.hms.hospital_management_system.models.Appointment.AppointmentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import com.hms.hospital_management_system.services.AppointmentService;
import java.util.*;

@RestController
@RequestMapping("/api/patient")
public class PublicPatientController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/appointment/{phoneNumber}")
    public ResponseEntity<?> getAppointmentByPhoneNumber(@PathVariable String phoneNumber) {
        try {
            System.out.println("Fetching Appointments for: " + phoneNumber);

            // Fetch appointments from the service
            List<Appointment> appointments = appointmentService.findByPhoneNumber(phoneNumber);

            // Filter only the ones that are not completed (status = BOOKED)
            List<Appointment> incompleteAppointments = new ArrayList<>();
            System.out.println(appointments.size());
            for (Appointment appointment : appointments) {
                if (appointment.getAppointmentStatus() == AppointmentStatus.BOOKED) {
                    incompleteAppointments.add(appointment);
                }
            }

            // If no appointments found, return 404 response
            if (incompleteAppointments.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", "No appointments found for phone number: " + phoneNumber
                ));
            }

            // Return the filtered list
            return ResponseEntity.ok(Map.of(
                "success", true,
                "appointments", incompleteAppointments
            ));

        } catch (Exception e) {
            // Handle any unexpected errors
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Error retrieving appointments: " + e.getMessage()
            ));
        }
    }
}

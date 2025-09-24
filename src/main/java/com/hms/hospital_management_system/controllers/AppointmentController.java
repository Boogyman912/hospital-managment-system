package com.hms.hospital_management_system.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hms.hospital_management_system.models.Appointment;
import com.hms.hospital_management_system.services.AppointmentService;
import com.hms.hospital_management_system.models.Patient;
import java.util.List;
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    
    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/doc/:id")
    public List<Patient> getAppointmentById(@PathVariable Long id) {
        // logic to get all the appointments of Doctor with id
        return appointmentService.getAppointmentById(id);
    }

    
    @PostMapping("/book/{id}")
    public void bookAppointment(@PathVariable Long id) {
        // logic to book an appointment with doctor with id
        // return "Book an appointment";
        appointmentService.bookAppointment(id);
    }
}

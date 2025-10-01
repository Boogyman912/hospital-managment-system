package com.hms.hospital_management_system.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hms.hospital_management_system.models.Doctor;
import java.util.List;
import com.hms.hospital_management_system.services.DoctorService;
@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping("/")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorService.getDoctorById(id);
        if (doctor != null) {
            return ResponseEntity.ok(doctor);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/create")
    public ResponseEntity<String> createDoctor(@RequestBody Doctor doctor){
        try {
            doctorService.createDoctor(doctor);
            return ResponseEntity.ok("Doctor created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating doctor: " + e.getMessage());
        }
    }

    // ADDED: Bulk create endpoint to add multiple doctors in one request (JSON array)
    @PostMapping("/bulk")
    public ResponseEntity<String> createDoctors(@RequestBody List<Doctor> doctors){
        try {
            int saved = doctorService.createDoctors(doctors);
            return ResponseEntity.ok("Doctors created successfully: " + saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating doctors: " + e.getMessage());
        }
    }

    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<Doctor>> getDoctorsBySpecialization(@PathVariable String specialization) {
        List<Doctor> doctors = doctorService.getDoctorsBySpecialization(specialization);
        return ResponseEntity.ok(doctors);
    }


}

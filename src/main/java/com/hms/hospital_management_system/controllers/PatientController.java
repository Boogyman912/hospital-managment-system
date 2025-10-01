package com.hms.hospital_management_system.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hms.hospital_management_system.models.Patient;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.hms.hospital_management_system.services.PatientService;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    
    @Autowired
    private PatientService patientService;

    @GetMapping("/")
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Patient patient = patientService.getPatientById(id);
        if (patient != null) {
            return ResponseEntity.ok(patient);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/create")
    public ResponseEntity<String> createPatient(@RequestBody Patient patient){
        try {
            patientService.createPatient(patient);
            return ResponseEntity.ok("Patient created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating patient: " + e.getMessage());
        }
    }


}

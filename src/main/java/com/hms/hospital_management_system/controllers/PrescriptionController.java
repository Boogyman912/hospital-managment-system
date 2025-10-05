package com.hms.hospital_management_system.controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import java.util.*;
import com.hms.hospital_management_system.models.Prescription;
import com.hms.hospital_management_system.services.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
@RestController
@RequestMapping("/api/staff/prescriptions")

public class PrescriptionController {
    @Autowired
    private PrescriptionService prescriptionService;

    //why is ResponseEntity<?> used?
    //because we want to return a response entity with a body of any type
    @GetMapping("/doctor/{doctor_id}/patient/{patient_id}")
    public ResponseEntity<?> getPrescriptionsByDoctorAndPatient(@PathVariable Long doctor_id, @PathVariable Long patient_id) {
        try {
            List<Prescription> prescriptions = prescriptionService.getPrescriptionsByDoctorAndPatient(doctor_id, patient_id);
            return ResponseEntity.ok(prescriptions);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving prescriptions: " + e.getMessage());
        }
    }

    @GetMapping("/doctor/{doctor_id}/patient/{patient_id}/date/{date_issued}")
    public ResponseEntity<?> getPrescriptionsByDoctorAndPatientAndDateIssued(@PathVariable Long doctor_id, @PathVariable Long patient_id, @PathVariable LocalDate date_issued) {
        try {
            List<Prescription> prescriptions = prescriptionService.getPrescriptionsByDoctorAndPatientAndDateIssued(doctor_id, patient_id, date_issued);
            return ResponseEntity.ok(prescriptions);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving prescriptions: " + e.getMessage());
        }
    }

    @GetMapping("/doctor/{doctor_id}")
    public ResponseEntity<?> getPrescriptionsByDoctor(@PathVariable Long doctor_id) {
        try {
            List<Prescription> prescriptions = prescriptionService.getPrescriptionsByDoctor(doctor_id);
            return ResponseEntity.ok(prescriptions);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving prescriptions: " + e.getMessage());
        }
    }

    @GetMapping("/patient/{patient_id}")
    public ResponseEntity<?> getPrescriptionsByPatient(@PathVariable Long patient_id) {
        try {
            List<Prescription> prescriptions = prescriptionService.getPrescriptionsByPatient(patient_id);
            return ResponseEntity.ok(prescriptions);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving prescriptions: " + e.getMessage());
        }
    }

    // @PostMapping("/create")
    // public ResponseEntity<Map<String, Object>> createPrescription(@RequestBody Prescription prescription) {
    //     Map<String, Object> response = new HashMap<>();
    //     try {
    //         Prescription createdPrescription = prescriptionService.createPrescription(prescription);
    //         response.put("message", "Prescription created successfully");
    //         response.put("prescription", createdPrescription);
    //         return ResponseEntity.ok(response);
    //     } catch (Exception e) {
    //         response.put("message", "Error creating prescription: " + e.getMessage());
    //         response.put("prescription", null);
    //         return ResponseEntity.status(500).body(response);
    //     }
    // }
    
    

    @GetMapping("/all")
    public ResponseEntity<?> getAllPrescriptions() {
        try {
            List<Prescription> prescriptions = prescriptionService.getAllPrescriptions();
            return ResponseEntity.ok(prescriptions);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving prescriptions: " + e.getMessage());
        }
    }


    
}

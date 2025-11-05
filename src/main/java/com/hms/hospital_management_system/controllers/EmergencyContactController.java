package com.hms.hospital_management_system.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.hms.hospital_management_system.services.EmergencyContactService;
import java.util.*;
import com.hms.hospital_management_system.dto.EmergencyContactAddReq;
import com.hms.hospital_management_system.models.EmergencyContact;
import com.hms.hospital_management_system.models.Patient;
import com.hms.hospital_management_system.services.PatientService;

@RestController
@RequestMapping("/api/staff/emergency-contacts")
public class EmergencyContactController {
    @Autowired
    private EmergencyContactService emergencyContactService;
    @Autowired
    private PatientService patientService;

    @GetMapping("/patient/{patientPhoneNumber}")
    public ResponseEntity<?> getEmergencyContactByPatientId(
            @PathVariable String patientPhoneNumber) {
        try {
            // Step 1: Find the patient by phone number
            Optional<Patient> patientOpt = patientService.findByPhoneNumber(patientPhoneNumber);

            if (patientOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("❌ Patient with phone number " + patientPhoneNumber + " not found.");
            }

            // Step 2: Get patient ID and fetch emergency contacts
            Long patientId = patientOpt.get().getPatientId();
            List<EmergencyContact> contacts =
                    emergencyContactService.getEmergencyContactByPatientId(patientId);

            // Step 3: Return proper response
            if (contacts == null || contacts.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("⚠️ No emergency contacts found for patient ID: " + patientId);
            }

            return ResponseEntity.ok(contacts);

        } catch (Exception e) {
            // Step 4: Handle unexpected exceptions gracefully
            System.err.println("Error fetching emergency contacts: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("🚨 An unexpected error occurred while retrieving emergency contacts.");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> addEmergencyContact(@RequestBody EmergencyContactAddReq req) {
        try {
            // Step 1: Validate input
            if (req.getName() == null || req.getRelation() == null || req.getPhoneNumber() == null
                    || req.getPatientPhoneNumber() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        "⚠️ All fields (name, relation, phoneNumber, patientPhoneNumber) are required.");
            }

            // Step 2: Find patient by phone number
            Optional<Patient> patientOpt =
                    patientService.findByPhoneNumber(req.getPatientPhoneNumber());
            if (patientOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("❌ Patient with phone number " + req.getPatientPhoneNumber()
                                + " not found.");
            }

            // Step 3: Create and populate EmergencyContact entity
            EmergencyContact newContact = new EmergencyContact();
            newContact.setName(req.getName());
            newContact.setRelation(req.getRelation());
            newContact.setPhoneNumber(req.getPhoneNumber());
            newContact.setPatient(patientOpt.get());

            // Step 4: Save the contact using your service
            EmergencyContact savedContact =
                    emergencyContactService.addEmergencyContact(newContact);

            // Step 5: Return success response
            return ResponseEntity.status(HttpStatus.CREATED).body(savedContact);

        } catch (Exception e) {
            // Step 6: Handle unexpected errors
            System.err.println("Error while creating emergency contact: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("🚨 An unexpected error occurred while creating emergency contact.");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEmergencyContact(@PathVariable Long id) {
        try {
            emergencyContactService.deleteEmergencyContact(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}

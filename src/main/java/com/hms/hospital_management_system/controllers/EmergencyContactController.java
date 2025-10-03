package com.hms.hospital_management_system.controllers;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.hms.hospital_management_system.services.EmergencyContactService;
import java.util.*;
import com.hms.hospital_management_system.models.EmergencyContact;

@RestController
@RequestMapping("/api/emergency-contacts")
public class EmergencyContactController {
    @Autowired
    private EmergencyContactService emergencyContactService;

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<EmergencyContact>> getEmergencyContactByPatientId(@PathVariable Long patientId) {
        List<EmergencyContact> contact = emergencyContactService.getEmergencyContactByPatientId(patientId);
        if (contact != null) {
            return ResponseEntity.ok(contact);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<EmergencyContact> addEmergencyContact(@RequestBody EmergencyContact contact) {
        EmergencyContact createdContact = emergencyContactService.addEmergencyContact(contact);
        return ResponseEntity.ok(createdContact);
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

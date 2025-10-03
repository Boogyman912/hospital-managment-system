package com.hms.hospital_management_system.controllers;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.*;
import com.hms.hospital_management_system.models.Inpatient;
import com.hms.hospital_management_system.services.InpatientService;


@RestController
@RequestMapping("/api/inpatients")
public class InpatientController {

    @Autowired
    private InpatientService inpatientService;

    @GetMapping("/all")
    public ResponseEntity<List<Inpatient>> getAllInpatients() {
        try {
            List<Inpatient> inpatients = inpatientService.getAllInpatients();
            return ResponseEntity.ok(inpatients);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Inpatient>> getInpatientsByPatientId(@PathVariable Long patientId) {
        try {
            List<Inpatient> inpatients = inpatientService.getInpatientsByPatientId(patientId);
            return ResponseEntity.ok(inpatients);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<Inpatient>> getInpatientsByRoomId(@PathVariable Long roomId) {
        try {
            List<Inpatient> inpatients = inpatientService.getInpatientsByRoomId(roomId);
            return ResponseEntity.ok(inpatients);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/inpatient/{id}")
    public ResponseEntity<Inpatient> getInpatientById(@PathVariable Long id) throws RuntimeException {
        try {
            Inpatient inpatient = inpatientService.getInpatientById(id);
            if (inpatient != null) {
                return ResponseEntity.ok(inpatient);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
             throw new RuntimeException( e.getMessage());
        }
    }

    @PostMapping("/admit")
    public ResponseEntity<Inpatient> admitPatient(@RequestBody Inpatient inpatient) throws RuntimeException {
        try {
            Inpatient admittedInpatient = inpatientService.admitPatient(inpatient);
            return ResponseEntity.ok(admittedInpatient);
        } catch (Exception e) {
            throw new RuntimeException( e.getMessage());
        }
    }

    @PostMapping("/updateRoom/{id}")
    public ResponseEntity<Inpatient> updateInpatientRoom(@PathVariable Long id, @RequestBody Inpatient inpatient) throws RuntimeException {
        try {
            Inpatient updatedInpatient = inpatientService.updateInpatientRoom(id, inpatient.getRoom());
            return ResponseEntity.ok(updatedInpatient);
        } catch (Exception e) {
            throw new RuntimeException( e.getMessage());
        }
    }

    @PostMapping("/discharge_in_patient/{id}")
    public ResponseEntity<String> dischargeInPatient(@PathVariable Long id) throws RuntimeException {
        try {
            inpatientService.dischargeInpatient(id);
            return ResponseEntity.ok("Inpatient discharged successfully");
        } catch (Exception e) {
            throw new RuntimeException( e.getMessage());
        }
    }


}

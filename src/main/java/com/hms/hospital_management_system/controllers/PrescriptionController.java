package com.hms.hospital_management_system.controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import java.util.List;
import com.hms.hospital_management_system.models.Prescription;
import com.hms.hospital_management_system.services.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
@RestController
@RequestMapping("/api/prescriptions")

public class PrescriptionController {
    @Autowired
    private PrescriptionService prescriptionService;

    @GetMapping("/doctor/{doctor_id}/patient/{patient_id}")
    public ResponseEntity<List<Prescription>> getPrescriptionsByDoctorAndPatient(@PathVariable Long doctor_id, @PathVariable Long patient_id) {
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByDoctorAndPatient(doctor_id, patient_id);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/doctor/{doctor_id}/patient/{patient_id}/date/{date_issued}")
    public ResponseEntity<List<Prescription>> getPrescriptionsByDoctorAndPatientAndDateIssued(@PathVariable Long doctor_id, @PathVariable Long patient_id, @PathVariable LocalDate date_issued) {
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByDoctorAndPatientAndDateIssued(doctor_id, patient_id, date_issued);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/doctor/{doctor_id}")
    public ResponseEntity<List<Prescription>> getPrescriptionsByDoctor(@PathVariable Long doctor_id) {
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByDoctor(doctor_id);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/patient/{patient_id}")
    public ResponseEntity<List<Prescription>> getPrescriptionsByPatient(@PathVariable Long patient_id) {
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByPatient(patient_id);
        return ResponseEntity.ok(prescriptions);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createPrescription(@RequestBody Prescription prescription) {
        prescriptionService.createPrescription(prescription);
        return ResponseEntity.ok("Prescription created successfully");
    }
    
    @PatchMapping("/update/{prescription_id}")
    public ResponseEntity<String> updatePrescription(@PathVariable Long prescription_id, @RequestBody Prescription prescription) {
        List<String> medications = prescription.getMedications();
        List<String> labTests = prescription.getLabTests();
        String instructions = prescription.getInstructions();
        LocalDate date_issued = prescription.getDateIssued();
        prescriptionService.updatePrescription(prescription_id, medications, labTests, instructions, date_issued);
        return ResponseEntity.ok("Prescription updated successfully");
    }

    @DeleteMapping("/delete/{prescription_id}")
    public ResponseEntity<String> deletePrescription(@PathVariable Long prescription_id) {
        prescriptionService.deletePrescription(prescription_id);
        return ResponseEntity.ok("Prescription deleted successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Prescription>> getAllPrescriptions() {
        List<Prescription> prescriptions = prescriptionService.getAllPrescriptions();
        return ResponseEntity.ok(prescriptions);
    }


    
}

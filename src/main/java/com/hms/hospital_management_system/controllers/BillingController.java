package com.hms.hospital_management_system.controllers;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.hms.hospital_management_system.services.InpatientService;
import java.util.*;
import com.hms.hospital_management_system.models.Billing;
import com.hms.hospital_management_system.services.BillingService;
@RestController
@RequestMapping("/api/billing")
public class BillingController {

    @Autowired
    private BillingService billingService;
    @Autowired
    private InpatientService inpatientService;

    @PostMapping("/generateforprescription/{prescription_id}")
    public ResponseEntity<Billing> generateBillByPrescription(@PathVariable Long prescription_id) {
        try {
            Billing bill = billingService.generateBillByPrescriptionId(prescription_id);
            return ResponseEntity.ok(bill);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Billing>> getAllBills() {
        try {
            List<Billing> bills = billingService.getAllBills();
            return ResponseEntity.ok(bills);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/patient/{patient_id}")
    public ResponseEntity<List<Billing>> getBillsByPatientId(@PathVariable Long patient_id) {
        try {
            List<Billing> bills = billingService.getBillsByPatientId(patient_id);
            return ResponseEntity.ok(bills);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/prescription/{prescription_id}")
    public ResponseEntity<Billing> getBillsByPrescriptionId(@PathVariable Long prescription_id) {
        try {
            Billing bill = billingService.getBillByPrescriptionId(prescription_id);
            return ResponseEntity.ok(bill);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping("/patient/{patient_id}/unpaid")

    public ResponseEntity<List<Billing>> getUnpaidBillsByPatientId(@PathVariable Long patient_id) {
        try {
            List<Billing> unpaidBills = billingService.getUnpaidBillsByPatientId(patient_id);
            return ResponseEntity.ok(unpaidBills);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping("/generateforinpatient/{patient_id}")
    public ResponseEntity<Billing> generateBillByInpatient(@PathVariable Long patient_id) {
        try {
            Billing bill = inpatientService.generateInpatientBillForPatient(patient_id);
            return ResponseEntity.ok(bill);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/delete/{bill_id}")
    public ResponseEntity<String> deleteBill(@PathVariable Long bill_id) {
        try {
            billingService.deleteBill(bill_id);
            return ResponseEntity.ok("Bill deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error deleting bill");
        }
    }


}

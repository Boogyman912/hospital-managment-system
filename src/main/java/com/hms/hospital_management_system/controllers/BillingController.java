package com.hms.hospital_management_system.controllers;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.*;
import com.hms.hospital_management_system.models.Billing;
import com.hms.hospital_management_system.services.BillingService;
@RestController
@RequestMapping("/api/billing")
public class BillingController {

    @Autowired
    private BillingService billingService;

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

    @PostMapping("/pay/{bill_id}")
    public ResponseEntity<Boolean> payBill(@PathVariable Long bill_id) {
        try {
            Boolean paidBill = billingService.payBill(bill_id);
            return ResponseEntity.ok(paidBill);
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
    


}

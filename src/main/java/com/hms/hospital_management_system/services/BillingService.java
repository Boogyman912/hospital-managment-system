package com.hms.hospital_management_system.services;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.hms.hospital_management_system.jpaRepository.BillingRepository;
import com.hms.hospital_management_system.models.Billing;
import com.hms.hospital_management_system.models.Prescription;
//import inventory and labtests
import com.hms.hospital_management_system.jpaRepository.InventoryRepository;
import com.hms.hospital_management_system.jpaRepository.LabTestRepository;  
//importing services
import com.hms.hospital_management_system.services.InventoryService;
import com.hms.hospital_management_system.services.LabTestService;
import jakarta.transaction.Transactional;
import com.hms.hospital_management_system.models.Inventory;
import com.hms.hospital_management_system.models.LabTest;
import java.time.LocalDate;
import java.util.*;

@Service
public class BillingService {

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private LabTestService labTestService;

    @Transactional
    public Billing generateBillByPrescriptionId(Long prescription_Id) {
        try {
            Billing billing = new Billing();
            Billing existingBilling = billingRepository.findBillByPrescriptionId(prescription_Id);
            if (existingBilling == null || existingBilling.getPrescription() == null) {
            throw new RuntimeException("Prescription not found for ID: " + prescription_Id);
            }
            billing.setPrescription(existingBilling.getPrescription());
            Double amount = 0.0;
            List<Map<String, String>> meds = billing.getPrescription().getMedications();
            List<Map<String, String>> labTest = billing.getPrescription().getLabTests();

            for (Map<String, String> med : meds) {
            Inventory item = inventoryService.getInventoryByItemNameAndBrandName(med.get("itemName"), med.get("brandName"));
            if (item != null) {
                Double unitPrice = item.getUnitPrice();
                Integer quantity = Integer.parseInt(med.get("quantity"));
                quantity = quantity >= item.getQuantity() ? item.getQuantity() : quantity; // Ensure we don't go negative
                amount += unitPrice * quantity;
                inventoryService.decreaseQuantity(item.getItemId(), quantity);
            }
            }

            for (Map<String, String> test : labTest) {
            LabTest lab = labTestService.getLabTestByNameAndType(test.get("testName"), test.get("testType"));
            if (lab != null) {
                Double testCost = lab.getTestCost();
                amount += testCost;
            }
            }

            billing.setTotalAmount(amount);
            billing.setStatus(Billing.Status.UNPAID);
            billing.setPaymentDate(null);
            billingRepository.save(billing);
            // i also want to generate a pdf bill and send it to the patient whatsapp number
            // how do i do it?
            // tell me
            return billing;
        } catch (Exception e) {
            // Log the error or handle it as needed
            System.err.println("Error generating bill: " + e.getMessage());
            return null;
        }
    }

    public void updateBillStatus(Long billId, String status) {
        billingRepository.updateBillStatus(billId, status);
    }

    public List<Billing> getBillsByPatientId(Long patientId) {
        return billingRepository.findBillsByPatientId(patientId);
    }

    public Billing getBillByPrescriptionId(Long prescriptionId) {
        return billingRepository.findBillByPrescriptionId(prescriptionId);
    }

    public List<Billing> getAllBills() {
        return billingRepository.findAll();
    }

    public boolean payBill(Long billId) {
        Billing bill = billingRepository.findById(billId).orElse(null);
        if (bill == null || bill.getStatus() == Billing.Status.PAID) {
            return false;
        }

        //implement payment gateway here
        /*
         * Simulate payment gateway integration.
         * In a real-world scenario, you would call an external payment API here.
         * For demonstration, we'll assume payment is always successful.
         */

        // Example: Simulate payment processing delay
        try {
            Thread.sleep(1000); // Simulate network/payment processing delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }

        // If integrating with a real payment gateway, you would:
        // 1. Prepare payment request with bill details.
        // 2. Send request to payment provider (e.g., Stripe, Razorpay, PayPal).
        // 3. Handle response and update bill status accordingly.

        // For now, assume payment is successful.
        
        bill.setStatus(Billing.Status.PAID);
        LocalDate today = LocalDate.now();
        bill.setPaymentDate(today);
        billingRepository.save(bill);
        return true;
    }
    public List<Billing> getUnpaidBillsByPatientId(Long patientId) {
        List<Billing> allBills = billingRepository.findBillsByPatientId(patientId);
        List<Billing> unpaidBills = new ArrayList<>();
        for (Billing bill : allBills) {
            if (bill.getStatus() == Billing.Status.UNPAID) {
                unpaidBills.add(bill);
            }
        }
        return unpaidBills;
    }
}

package com.hms.hospital_management_system.services;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.hms.hospital_management_system.jpaRepository.BillingRepository;
import com.hms.hospital_management_system.models.Billing;
import jakarta.transaction.Transactional;
import com.hms.hospital_management_system.models.Inventory;
import com.hms.hospital_management_system.models.LabTest;
import java.time.LocalDate;
import com.hms.hospital_management_system.models.Inpatient;
import com.hms.hospital_management_system.jpaRepository.InpatientRepository;
import java.util.*;

@Service
public class BillingService {

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private LabTestService labTestService;

    @Autowired
    private InpatientRepository inpatientRepository;

    @Transactional
    public Billing generateBillByPrescriptionId(Long prescription_Id) {
        try {
            Billing billing = new Billing();
            
            billing.setPrescription( prescriptionService.getPrescriptionById(prescription_Id));
            Double amount = 0.0;
            List<Map<String, String>> meds = billing.getPrescription().getMedications();
            List<Map<String, String>> labTest = billing.getPrescription().getLabTests();
            
            System.out.println("\n\n\n\nMedications to process: " + meds.size());
            
            // Cache inventory items to avoid duplicate lookups
            Map<String, Inventory> inventoryCache = new HashMap<>();
            
            // Process medications - lookup inventory items once and cache them
            for (Map<String, String> med : meds) {
                System.out.println("Processing medication: " + med.get("itemName") + " - " + med.get("brandName"));
                String cacheKey = med.get("itemName") + "|" + med.get("brandName");
                
                Inventory item = inventoryCache.get(cacheKey);
                if (item == null) {
                    item = inventoryService.getInventoryByItemNameAndBrandName(med.get("itemName"), med.get("brandName"));
                    if (item != null) {
                        inventoryCache.put(cacheKey, item);
                    }
                }
                
                if (item != null) {
                    Double unitPrice = item.getUnitPrice();
                    Integer quantity = Integer.parseInt(med.get("quantity"));
                    quantity = quantity >= item.getQuantity() ? item.getQuantity() : quantity; // Ensure we don't go negative
                    System.out.println("\n\n\n\n\n\n\n\n\n\nItem: " + item.getItemName() + ", Unit Price: " + unitPrice + ", Quantity: " + quantity);
                    amount += unitPrice * quantity;
                    // Note: decreaseQuantity uses @Modifying query which is efficient
                    // Individual updates are acceptable here as they're batched within the transaction
                    inventoryService.decreaseQuantity(item.getItemId(), quantity);
                }
            }

            // Process lab tests
            for (Map<String, String> test : labTest) {
                LabTest lab = labTestService.getLabTestByNameAndType(test.get("testName"), test.get("testType"));
                if (lab != null) {
                    Double testCost = lab.getTestCost();
                    System.out.println("Lab Test: " + lab.getTestName() + ", Test Cost: " + testCost);
                    amount += testCost;
                }
            }
            
            System.out.println("For prescription with prescriptionId "+ prescription_Id +" Total amount calculated: " + amount);
            billing.setPatient(prescriptionService.getPrescriptionById(prescription_Id).getPatient());
            billing.setTotalAmount(amount);
            billing.setStatus(Billing.Status.UNPAID);
            billing.setPaymentDate(null);
            billingRepository.save(billing);
            return billing;
        } catch (Exception e) {
            // Log the error or handle it as needed
            System.err.println("Error generating bill: " + e.getMessage());
            return null;
        }
    }

    // to generate bill for all the rooms a patient stayed in
    @Transactional
    public Billing generateInpatientBill(List<Inpatient> inpatients) throws RuntimeException{
        try{
            Billing billing = new Billing();
            Double totalAmount = 0.0;
            for(Inpatient inpatient : inpatients){
                if(inpatient.getDischargeDate() == null){
                    inpatient.setDischargeDate(LocalDate.now());
                }
                Long daysStayed = java.time.temporal.ChronoUnit.DAYS.between(inpatient.getAdmissionDate(), inpatient.getDischargeDate());
                daysStayed = daysStayed == 0 ? 1 : daysStayed; // Minimum 1 day charge
                Double roomCharge = inpatient.getRoom().getPricePerDay();
                totalAmount += daysStayed * roomCharge;
                if(billing.getPatient() == null){
                    billing.setPatient(inpatient.getPatient());
                }
            }
            billing.setInpatients(inpatients);
            billing.setTotalAmount(totalAmount);
            billing.setStatus(Billing.Status.UNPAID);
            billing.setPaymentDate(null);
            billingRepository.save(billing);
            return billing;
        }catch(Exception e){
            throw new RuntimeException("Error generating inpatient bill: " + e.getMessage());
        }
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
         * 
         * If integrating with a real payment gateway, you would:
         * 1. Prepare payment request with bill details.
         * 2. Send request to payment provider (e.g., Stripe, Razorpay, PayPal).
         * 3. Handle response and update bill status accordingly.
         */
        
        bill.setStatus(Billing.Status.PAID);

        if(bill.getInpatients()!= null){
            List<Inpatient> inpatients = bill.getInpatients();
            for(Inpatient inpatient : inpatients){
                inpatient.setIsBilled(true);
                inpatientRepository.save(inpatient);
            }
        }

        LocalDate today = LocalDate.now();
        bill.setPaymentDate(today);
        billingRepository.save(bill);
        return true;
    }
    public List<Billing> getUnpaidBillsByPatientId(Long patientId) {
        // Optimized: Use database query instead of filtering in Java
        return billingRepository.findUnpaidBillsByPatientId(patientId);
    }
    

    public void deleteBill(Long billId) {
        billingRepository.deleteById(billId);
    }
}
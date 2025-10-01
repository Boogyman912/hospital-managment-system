package com.hms.hospital_management_system.services;

import com.hms.hospital_management_system.jpaRepository.ReceiptRepository;
import com.hms.hospital_management_system.models.Appointment;
import com.hms.hospital_management_system.models.Receipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {
    
    @Autowired
    private ReceiptRepository receiptRepository;
    
    public Receipt processPayment(Appointment appointment, String paymentMethod, Double amount) {
        try {
            Receipt receipt = new Receipt();
            receipt.setAppointment(appointment);
            receipt.setAmount(amount);
            receipt.setPaymentMethod(paymentMethod);
            receipt.setTimestamp(LocalDateTime.now());
            receipt.setPdfUrl(generateReceiptPdf(receipt));
            
            Receipt savedReceipt = receiptRepository.save(receipt);
            
            // Update appointment payment status
            appointment.setPaymentStatus("PAID");
            appointment.setReceipt(savedReceipt);
            
            return savedReceipt;
        } catch (Exception e) {
            System.out.println("Error processing payment: " + e.getMessage());
            return null;
        }
    }
    
    private String generateReceiptPdf(Receipt receipt) {
        // In a real application, this would generate a PDF receipt
        // For now, we'll return a mock URL
        return "https://hospital-management.com/receipts/" + UUID.randomUUID().toString() + ".pdf";
    }
    
    public Receipt getReceiptByAppointmentId(Long appointmentId) {
        try {
            return receiptRepository.findByAppointmentAppointmentId(appointmentId);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}

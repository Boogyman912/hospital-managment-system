package com.hms.hospital_management_system.services;
import com.hms.hospital_management_system.jpaRepository.AppointmentRepository;
import com.hms.hospital_management_system.models.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private SlotService slotService;
    
    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private PatientService patientService;
    
    @Autowired
    private DoctorService doctorService;

    public List<Appointment> getAllAppointments() {
        try {
            return appointmentRepository.findAll();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
    
public List<Appointment> getAppointmentsByDoctorId(Long doctorId) {
    List<Appointment> filteredAppointments = new ArrayList<>();

    try {
        // Validate input
        if (doctorId == null) {
            throw new IllegalArgumentException("Doctor ID cannot be null.");
        }

        // Fetch appointments for the doctor
        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);

        if (appointments == null || appointments.isEmpty()) {
            System.out.println("No appointments found for doctor ID: " + doctorId);
            return filteredAppointments;
        }

        LocalDate today = LocalDate.now();

        // Filter appointments of today and after today
        filteredAppointments = appointments.stream()
                .filter(appointment -> {
                    try {
                        LocalDate date = appointment.getSlot() != null ? appointment.getSlot().getDate() : null;
                        return date != null && (date.isEqual(today) || date.isAfter(today));
                    } catch (Exception ex) {
                        System.err.println("Error processing appointment ID " + appointment.getAppointmentId() + ": " + ex.getMessage());
                        return false;
                    }
                })
                .toList();

    } catch (IllegalArgumentException ex) {
        System.err.println("Invalid argument: " + ex.getMessage());
    } catch (DataAccessException ex) {
        System.err.println("Database access error while fetching appointments: " + ex.getMessage());
    } catch (Exception ex) {
        System.err.println("Unexpected error while fetching appointments for doctor ID " + doctorId + ": " + ex.getMessage());
    }

    return filteredAppointments;
}


    
    public List<Appointment> getAppointmentsByPatientId(Long patientId) {
        try {
            return appointmentRepository.findByPatientPatientId(patientId);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
    
    public Appointment bookAppointment(Long patientId, Long doctor_id, Long slotId, boolean isOnline, String meetLink) {
        try {
            // Get patient and doctor
            Patient patient = patientService.getPatientById(patientId);
            Doctor doctor = doctorService.getDoctorById(doctor_id);
            
            if (patient == null || doctor == null) {
                System.out.println("Patient or Doctor not found");
                return null;
            }
            
            // Hold the slot
            Slot slot = slotService.holdSlot(slotId, 15); // Hold for 15 minutes
            if (slot == null) {
                System.out.println("Slot not available or already booked");
                return null;
            }
            
            // Create appointment
            Appointment appointment = new Appointment();
            appointment.setPatient(patient);
            appointment.setDoctor(doctor);
            appointment.setSlot(slot);
            appointment.setBookingTime(LocalDate.now());
            appointment.setPaymentStatus("PENDING");
            appointment.setAppointmentStatus(Appointment.AppointmentStatus.BOOKED);
            appointment.setIsOnline(isOnline);
            appointment.setMeetLink(meetLink);
            
            Appointment savedAppointment = appointmentRepository.save(appointment);
            
            // Book the slot
            slotService.bookSlot(slotId);
            
            return savedAppointment;
        } catch (Exception e) {
            System.out.println("Error booking appointment: " + e.getMessage());
            return null;
        }
    }
    
    public Appointment confirmPayment(Long appointmentId, String paymentMethod, Double amount) {
        try {
            Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
            if (appointment == null) {
                System.out.println("Appointment not found");
                return null;
            }
            
            if (!"PENDING".equals(appointment.getPaymentStatus())) {
                System.out.println("Payment already processed");
                return appointment;
            }
            
            // Process payment
            Receipt receipt = paymentService.processPayment(appointment, paymentMethod, amount);
            if (receipt != null) {
                appointment.setPaymentStatus("PAID");
                appointment.setReceipt(receipt);
                return appointmentRepository.save(appointment);
            }
            
            return null;
        } catch (Exception e) {
            System.out.println("Error confirming payment: " + e.getMessage());
            return null;
        }
    }
    
    public void cancelAppointment(Long appointmentId) {
        try {
            Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
            if (appointment != null) {
                appointment.setAppointmentStatus(Appointment.AppointmentStatus.CANCELLED);
                appointmentRepository.save(appointment);
                
                // Release the slot
                if (appointment.getSlot() != null) {
                    slotService.releaseSlot(appointment.getSlot().getSlotId());
                }
            }
        } catch (Exception e) {
            System.out.println("Error cancelling appointment: " + e.getMessage());
        }
    }
    
    public Appointment getAppointmentById(Long appointmentId) {
        try {
            return appointmentRepository.findById(appointmentId).orElse(null);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public Boolean isAppointmentWithDoctor(Long appointmentId,Long doctorId) throws RuntimeException{
        Long doctor_id = appointmentRepository.findById(appointmentId)
        .orElseThrow(() -> new RuntimeException("Appointment doesn't exist"))
        .getDoctor()
        .getDoctor_id();

        return (doctorId == doctor_id);
    }

    public List<Appointment> findByPhoneNumber(String phoneNumber) throws RuntimeException{
        Long patientId = patientService.findByPhoneNumber(phoneNumber).orElseThrow(()-> new UsernameNotFoundException("No patient exist with this phone number "+phoneNumber)).getPatientId();
        List<Appointment> appointment = appointmentRepository.findByPatientPatientId(patientId);
        return appointment;
    }
}

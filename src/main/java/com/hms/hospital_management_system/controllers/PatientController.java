package com.hms.hospital_management_system.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import com.hms.hospital_management_system.models.Patient;
import com.hms.hospital_management_system.models.Prescription;
import com.hms.hospital_management_system.models.Slot;
import com.hms.hospital_management_system.models.Appointment.AppointmentStatus;
import com.hms.hospital_management_system.models.Billing;
import com.hms.hospital_management_system.models.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import com.hms.hospital_management_system.services.BillingService;
import com.hms.hospital_management_system.services.PatientService;
import com.hms.hospital_management_system.services.AppointmentService;
import com.hms.hospital_management_system.services.PrescriptionService;
import com.hms.hospital_management_system.services.SlotService;
import com.hms.hospital_management_system.services.DoctorService;
import com.hms.hospital_management_system.services.InpatientService;
import java.util.*;
import com.hms.hospital_management_system.models.Appointment;
import java.lang.RuntimeException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/home")
public class PatientController {

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private BillingService billingService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private InpatientService inpatientService;

    @Autowired
    private SlotService slotService;
    // @GetMapping("/")
    // public ResponseEntity<List<Patient>> getAllPatients() {
    // List<Patient> patients = patientService.getAllPatients();
    // return ResponseEntity.ok(patients);
    // }

    // @GetMapping("/{id}")
    // public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
    // Patient patient = patientService.getPatientById(id);
    // if (patient != null) {
    // return ResponseEntity.ok(patient);
    // }
    // return ResponseEntity.notFound().build();
    // }
    // **************************************************************
    // Assuming every patient his own phone number
    @PostMapping("/addpatient")
    public ResponseEntity<?> createPatient(@RequestBody Patient patient) {
        try {
            Optional<Patient> existingPatient =
                    patientService.findByPhoneNumber(patient.getPhoneNumber());
                    System.out.println(existingPatient);
            if (!existingPatient.isEmpty()) {
                return ResponseEntity.ok().body(
                        Map.of("message", "Patient already exist", "patient", existingPatient));
            }
            Patient createdPatient = patientService.createPatient(patient);
            return ResponseEntity.ok().body(
                    Map.of("message", "Patient created successfully", "patient", createdPatient));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating patient: " + e.getMessage());
        }
    }

    @GetMapping("/appointment/{phoneNumber}")
public ResponseEntity<?> getAppointmentByPhoneNumber(@PathVariable String phoneNumber) {
    try {
        System.out.println("Fetching Appointments for: " + phoneNumber);

        // Fetch appointments from the service
        List<Appointment> appointments = appointmentService.findByPhoneNumber(phoneNumber);

        // Filter only the ones that are not completed (status = BOOKED)
        List<Appointment> incompleteAppointments = new ArrayList<>();
        System.out.println(appointments.size());
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentStatus() == AppointmentStatus.BOOKED) {
                incompleteAppointments.add(appointment);
            }
        }

        // If no appointments found, return 404 response
        if (incompleteAppointments.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of(
                "success", false,
                "message", "No appointments found for phone number: " + phoneNumber
            ));
        }

        // Return the filtered list
        return ResponseEntity.ok(Map.of(
            "success", true,
            "appointments", incompleteAppointments
        ));

    } catch (Exception e) {
        // Handle any unexpected errors
        return ResponseEntity.status(500).body(Map.of(
            "success", false,
            "message", "Error retrieving appointments: " + e.getMessage()
        ));
    }
}


    @GetMapping("/patient/prescription/{phoneNumber}")
    public ResponseEntity<?> getPrescriptionsByPatient(@PathVariable String phoneNumber)
            throws RuntimeException {
        try {

            List<Prescription> prescriptions = prescriptionService
                    .getPrescriptionsByPatient(patientService.findByPhoneNumber(phoneNumber)
                            .orElseThrow(() -> new UsernameNotFoundException(
                                    "Patient with phone number " + phoneNumber + " not found."))
                            .getPatientId());
            return ResponseEntity.ok(prescriptions);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error retrieving prescriptions: " + e.getMessage());
        }
    }

    @PostMapping("/generatebillforprescription/{prescription_id}")
    public ResponseEntity<Billing> generateBillByPrescription(@PathVariable Long prescription_id) {
        try {
            Billing bill = billingService.generateBillByPrescriptionId(prescription_id);
            return ResponseEntity.ok(bill);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/patient/unpaid/bills/{phoneNumber}")

    public ResponseEntity<List<Billing>> getUnpaidBillsByPatientPhoneNumber(
            @PathVariable String phoneNumber) throws RuntimeException {
        try {
            Long patient_id = patientService.findByPhoneNumber(phoneNumber).orElseThrow(
                    () -> new UsernameNotFoundException("No user exist with this phone number"))
                    .getPatientId();
            List<Billing> unpaidBills = billingService.getUnpaidBillsByPatientId(patient_id);
            return ResponseEntity.ok(unpaidBills);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

     @PostMapping("/generatebillforinpatient/patient/{phoneNumber}")
    public ResponseEntity<Billing> generateBillByInpatient(@PathVariable Long patient_id) {
        try {
            Billing bill = inpatientService.generateInpatientBillForPatient(patient_id);
            return ResponseEntity.ok(bill);
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

    // public end points
    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        System.out.println("Yo wassup");
        List<Doctor> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/doctor/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorService.getDoctorById(id);
        if (doctor != null) {
            return ResponseEntity.ok(doctor);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/doctors/specializations")
    public ResponseEntity<List<String>> getAllSpecializations() {
        List<String> specializations = doctorService.getAllSpecializations();
        return ResponseEntity.ok(specializations);
    }

    @GetMapping("/doctors/specialization/{specialization}")
    public ResponseEntity<List<Doctor>> getDoctorsBySpecialization(
            @PathVariable String specialization) {
        List<Doctor> doctors = doctorService.getDoctorsBySpecialization(specialization);
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/doctor/{doctor_id}/date/{date}")
    public ResponseEntity<List<Slot>> getAvailableSlots(
            @PathVariable Long doctor_id, 
            @PathVariable String date) {
        LocalDate appointmentDate = LocalDate.parse(date);
        List<Slot> slots = slotService.getAvailableSlotsByDoctorAndDate(doctor_id, appointmentDate);
        return ResponseEntity.ok(slots);
    }

    @GetMapping("/doctor-slots/{doctor_id}")

    public ResponseEntity<List<Slot>> getAvailableSlotsByDoctor(
            @PathVariable Long doctor_id) {
        List<Slot> slots = slotService.getAvailableSlotsByDoctor(doctor_id);
        return ResponseEntity.ok(slots);
    }



}

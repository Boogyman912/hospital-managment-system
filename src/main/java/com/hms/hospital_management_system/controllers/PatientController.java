package com.hms.hospital_management_system.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import com.hms.hospital_management_system.models.Patient;
import com.hms.hospital_management_system.models.Prescription;
import com.hms.hospital_management_system.models.Appointment.AppointmentStatus;
import com.hms.hospital_management_system.models.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import com.hms.hospital_management_system.services.BillingService;
import com.hms.hospital_management_system.services.PatientService;
import com.hms.hospital_management_system.services.AppointmentService;
import com.hms.hospital_management_system.services.PrescriptionService;
import com.hms.hospital_management_system.services.DoctorService;
import java.util.*;
import com.hms.hospital_management_system.models.Appointment;
import java.lang.RuntimeException;

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
//**************************************************************
    // Assuming every patient his own phone number
    @PostMapping("/addpatient")
    public ResponseEntity<?> createPatient(@RequestBody Patient patient) {
        try {
            Optional<Patient> existingPatient =
                    patientService.findByPhoneNumber(patient.getPhoneNumber());
            if (existingPatient != null) {
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

    @GetMapping("/appointment/{phone_number}")
    // those are not completed
    public ResponseEntity<List<Appointment>> getAppoinmentByPhoneNumber(
            @PathVariable String phoneNumber) throws RuntimeException {

        // 1. Get the list of appointments. Assume the service returns a List<Appointment>
        List<Appointment> appointments = appointmentService.findByPhoneNumber(phoneNumber);
        List<Appointment> incompleteAppointments = new ArrayList<>();
        for(Appointment appointment : appointments){
            if(appointment.getAppointmentStatus() == AppointmentStatus.BOOKED){
                incompleteAppointments.add(appointment);
            }
        }
        

        // 2. Check if the list is empty (no appointments found for the number)
        if (incompleteAppointments.isEmpty()) {
            // Throw a Spring exception that maps to HTTP 404 Not Found
            throw new UsernameNotFoundException(
                    "No appointments found for phone number: " + phoneNumber);
        }

        // 3. Return the list with HTTP 200 OK
        return ResponseEntity.ok(incompleteAppointments);
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

    @GetMapping("/doctors/specialization/{specialization}")
    public ResponseEntity<List<Doctor>> getDoctorsBySpecialization(
            @PathVariable String specialization) {
        List<Doctor> doctors = doctorService.getDoctorsBySpecialization(specialization);
        return ResponseEntity.ok(doctors);
    }


}

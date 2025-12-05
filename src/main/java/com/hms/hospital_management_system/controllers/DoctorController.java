package com.hms.hospital_management_system.controllers;

import org.springframework.security.access.AccessDeniedException; // Recommended for 403 errors
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.hms.hospital_management_system.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hms.hospital_management_system.models.Appointment;
import com.hms.hospital_management_system.models.Doctor;
import com.hms.hospital_management_system.models.Inventory;
import com.hms.hospital_management_system.models.LabTest;
import com.hms.hospital_management_system.models.Slot;
// import com.hms.hospital_management_system.models.Doctor;
import com.hms.hospital_management_system.models.Prescription;
// import com.hms.hospital_management_system.models.Slot;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import com.hms.hospital_management_system.services.AppointmentService;
// import com.hms.hospital_management_system.services.DoctorService;
import com.hms.hospital_management_system.services.FeedbackService;
import com.hms.hospital_management_system.services.InventoryService;
import com.hms.hospital_management_system.services.PrescriptionService;
import com.hms.hospital_management_system.services.SlotService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.hms.hospital_management_system.dto.FeedbackDTO;
import com.hms.hospital_management_system.jpaRepository.UserRepository;
import com.hms.hospital_management_system.services.DoctorService;
import com.hms.hospital_management_system.services.LabTestService;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {


        @Autowired
        private AppointmentService appointmentService;

        @Autowired
        private PrescriptionService prescriptionService;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private SlotService slotService;

        @Autowired
        private FeedbackService feedbackService;

        @Autowired
        private DoctorService doctorService;
        @Autowired
        private InventoryService inventoryService;
        @Autowired
        private LabTestService labTestService;

        // Doctor can only see their appointments
        @GetMapping("/appointments")
        public ResponseEntity<List<Appointment>> getMyAppointments(Authentication auth)
                        throws RuntimeException {
                String doctorUsername = auth.getName();
                // getting doctor_id from doctorUserName
                // Fetch user using Optional
                User doctor = userRepository.findByUsername(doctorUsername).orElseThrow(
                                () -> new UsernameNotFoundException("Doctor with username '"
                                                + doctorUsername + "' not found"));
                Long doctorId = doctor.getDoctorId();
                List<Appointment> appointments =
                                appointmentService.getAppointmentsByDoctorId(doctorId);
                return ResponseEntity.ok(appointments);
        }

        @GetMapping("/allinventory")
        public ResponseEntity<List<Inventory>> getAllInventory() {
                try {
                        List<Inventory> inventory = inventoryService.getAllInventory();
                        return ResponseEntity.ok(inventory);
                } catch (Exception e) {
                        return ResponseEntity.internalServerError().build();
                }
        }

        @GetMapping("/alllabtests")
        public ResponseEntity<List<LabTest>> testLabTestController() {
                return ResponseEntity.ok(labTestService.getAllLabTests());
        }

        @GetMapping("/feedbacks")
        public ResponseEntity<List<FeedbackDTO>> getFeedbacksByDoctorId(Authentication auth)
                        throws RuntimeException {
                Long doctorId = userRepository.findByUsername(auth.getName()).orElseThrow(
                                () -> new UsernameNotFoundException("Doctor not found"))
                                .getDoctorId();
                List<FeedbackDTO> feedbacks = feedbackService.getFeedbacksByDoctorId(doctorId);
                return ResponseEntity.ok(feedbacks);
        }

        // Doctor can only create prescription for patients who had an appointment with them
        @Transactional
        @PostMapping("/prescriptions/{appointment_id}")
        public ResponseEntity<String> createPrescription(@RequestBody Prescription prescription,
                        Authentication auth, @PathVariable Long appointment_id)
                        throws RuntimeException {
                String doctorUsername = auth.getName();
                User doctor = userRepository.findByUsername(doctorUsername).orElseThrow(
                                () -> new UsernameNotFoundException("Doctor with username '"
                                                + doctorUsername + "' not found"));
                Long doctorId = doctor.getDoctorId();

                if (!appointmentService.isAppointmentWithDoctor(appointment_id, doctorId)) {
                        return ResponseEntity.status(403).body(
                                        "Forbidden: Patient did not book appointment with you");
                }
                Appointment appointment = appointmentService.getAppointmentById(appointment_id);
                appointment.setAppointmentStatus(Appointment.AppointmentStatus.COMPLETED);
                prescription.setDoctor(doctorService.getDoctorById(doctorId));
                prescription.setPatient(appointment.getPatient());
                prescription.setDateIssued(LocalDate.now());
                prescriptionService.createPrescription(prescription);
                return ResponseEntity.ok("Prescription created successfully");
        }


        @PatchMapping("/update/prescription/{prescription_id}")

        public ResponseEntity<String> updatePrescription(Authentication auth,
                        @PathVariable Long prescription_id, @RequestBody Prescription prescription)
                        throws RuntimeException {

                // The try-catch block for the entire method is generally redundant (see note
                // below),
                // but we'll complete the logic within the existing structure.
                try {
                        String doctorUsername = auth.getName();
                        User doctor = userRepository.findByUsername(doctorUsername).orElseThrow(
                                        () -> new UsernameNotFoundException("Doctor with username '"
                                                        + doctorUsername + "' not found"));

                        // 1. Get the ID of the authenticated user
                        Long authenticatedDoctorId = doctor.getDoctorId();

                        // 2. Get the ID of the doctor who issued the prescription
                        Long issuingDoctorId =
                                        prescriptionService.getPrescriptionById(prescription_id)
                                                        .getDoctor().getDoctor_id();

                        if (authenticatedDoctorId != issuingDoctorId) {
                                // 🚨 COMPLETION: Throw an exception to deny access (HTTP 403
                                // Forbidden)
                                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                                                "You are not authorized to update this prescription, as you did not issue it.");
                        }

                        // --- If authorization succeeds, proceed with update ---

                        List<Map<String, String>> medications = prescription.getMedications();
                        List<Map<String, String>> labTests = prescription.getLabTests();
                        String instructions = prescription.getInstructions();
                        LocalDate date_issued = prescription.getDateIssued();

                        prescriptionService.updatePrescription(prescription_id, medications,
                                        labTests, instructions, date_issued);

                        return ResponseEntity.ok("Prescription updated successfully");

                } catch (ResponseStatusException rse) {
                        // Catch and rethrow ResponseStatusException to ensure the correct status
                        // code is used
                        throw rse;

                } catch (UsernameNotFoundException unfe) {
                        // Catch and rethrow Spring Security exceptions for proper handling
                        throw unfe;

                } catch (Exception e) {
                        // Generic fallback for unexpected errors (e.g., service failure, database
                        // issue)
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body("Error updating prescription: " + e.getMessage());
                }
        }

        @GetMapping("/prescription/{prescription_id}")
        public ResponseEntity<?> getPrescriptionById(@PathVariable Long prescription_id,
                        Authentication auth) {
                try {
                        String doctorUsername = auth.getName();
                        User doctor = userRepository.findByUsername(doctorUsername).orElseThrow(
                                        () -> new UsernameNotFoundException("Doctor with username '"
                                                        + doctorUsername + "' not found"));

                        // 1. Get the ID of the authenticated user
                        Long authenticatedDoctorId = doctor.getDoctorId();

                        // 2. Get the ID of the doctor who issued the prescription
                        Long issuingDoctorId =
                                        prescriptionService.getPrescriptionById(prescription_id)
                                                        .getDoctor().getDoctor_id();

                        if (authenticatedDoctorId != issuingDoctorId) {
                                // 🚨 COMPLETION: Throw an exception to deny access (HTTP 403
                                // Forbidden)
                                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                                                "You are not authorized to see this prescription, as you did not issue it.");
                        }
                        Prescription prescription =
                                        prescriptionService.getPrescriptionById(prescription_id);
                        if (prescription == null) {
                                return ResponseEntity.notFound().build();
                        }
                        return ResponseEntity.ok(prescription);
                } catch (Exception e) {
                        return ResponseEntity.status(500)
                                        .body("Error retrieving prescription: " + e.getMessage());
                }
        }

        @DeleteMapping("/delete/prescription/{prescription_id}")
        public ResponseEntity<String> deletePrescription(@PathVariable Long prescription_id,
                        Authentication auth) {
                try {
                        String doctorUsername = auth.getName();
                        User doctor = userRepository.findByUsername(doctorUsername).orElseThrow(
                                        () -> new UsernameNotFoundException("Doctor with username '"
                                                        + doctorUsername + "' not found"));

                        // 1. Get the ID of the authenticated user
                        Long authenticatedDoctorId = doctor.getDoctorId();

                        // 2. Get the ID of the doctor who issued the prescription
                        Long issuingDoctorId =
                                        prescriptionService.getPrescriptionById(prescription_id)
                                                        .getDoctor().getDoctor_id();

                        if (authenticatedDoctorId != issuingDoctorId) {
                                // 🚨 COMPLETION: Throw an exception to deny access (HTTP 403
                                // Forbidden)
                                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                                                "You are not authorized to delete this prescription, as you did not issue it.");
                        }
                        System.out.println("Deleting prescription with ID: " + prescription_id);
                        prescriptionService.deletePrescription(prescription_id);
                        return ResponseEntity.ok("Prescription deleted successfully");
                } catch (Exception e) {
                        return ResponseEntity.status(500)
                                        .body("Error deleting prescription: " + e.getMessage());
                }
        }


        // @GetMapping("/feedbacks")
        // public ResponseEntity<List<FeedbackDTO>> getFeedbacksByDoctorId(@PathVariable Long
        // doctorId,
        // Authentication auth) {

        // List<FeedbackDTO> feedbacks = feedbackService.getFeedbacksByDoctorId(doctorId);
        // return ResponseEntity.ok(feedbacks);
        // }

        // Doctor can see prescriptions they created
        @GetMapping("/prescriptions")
        public ResponseEntity<List<Prescription>> getMyPrescriptions(Authentication auth) {
                String doctorUsername = auth.getName();
                User doctor = userRepository.findByUsername(doctorUsername).orElseThrow(
                                () -> new UsernameNotFoundException("Doctor with username '"
                                                + doctorUsername + "' not found"));
                Long doctorId = doctor.getDoctorId();
                List<Prescription> prescriptions =
                                prescriptionService.getPrescriptionsByDoctor(doctorId);
                return ResponseEntity.ok(prescriptions);
        }

        // Doctor can manage their own time slots
        @PostMapping("/create-slots")
        public ResponseEntity<Map<String, Object>> createSlots(
                        @RequestBody Map<String, Object> slotRequest, Authentication auth)
                        throws RuntimeException {
                try {
                        System.out.println("Hello .....\ncreating slots");
                        String doctorUsername = auth.getName();
                        User doctor = userRepository.findByUsername(doctorUsername).orElseThrow(
                                        () -> new UsernameNotFoundException("Doctor with username '"
                                                        + doctorUsername + "' not found"));
                        Long doctorId = doctor.getDoctorId();
                        // Long doctor_id = Long.valueOf(slotRequest.get("doctor_id").toString());
                        // if (!doctorId.equals(doctor_id)) {
                        // return ResponseEntity.status(403).body(Map.of("success", false,
                        // "message",
                        // "You are not authorized to create slots for another doctor"));
                        // }
                        String dateStr = slotRequest.get("date").toString();
                        // CHANGED: Postman sends JSON arrays as List, not String[]. Convert safely.
                        @SuppressWarnings("unchecked")
                        List<String> timeSlotsList = (List<String>) slotRequest.get("timeSlots");
                        if (timeSlotsList == null) {
                                return ResponseEntity.badRequest().body(Map.of("success", false,
                                                "message",
                                                "timeSlots is required and must be an array of strings"));
                        }
                        String[] timeSlots = timeSlotsList.toArray(new String[0]);
                        Boolean isOnline = Boolean.valueOf(slotRequest.get("isOnline").toString());

                        LocalDate date = LocalDate.parse(dateStr);
                        // CHANGED: use return value to confirm how many were saved
                        // print all time slots
                        // System.out.println("Time slots: ");
                        // for (String time : timeSlots) {
                        // System.out.println("Time slot: " + time);
                        // }
                        int saved = slotService.createSlotsForDoctor(doctorId, date, timeSlots,
                                        isOnline);
                        if (saved == 0) {
                                return ResponseEntity.badRequest().body(Map.of("success", false,
                                                "message",
                                                "No slots were created. Check doctor_id, date, and timeSlots."));
                        }
                        return ResponseEntity.ok(Map.of("success", true, "message",
                                        "Slots created successfully", "count", saved));
                } catch (Exception e) {
                        return ResponseEntity.badRequest().body(Map.of("success", false, "message",
                                        "Error creating slots: " + e.getMessage()));
                }
        }

        @DeleteMapping("/delete-slot/{slotId}")
        public ResponseEntity<Map<String, Object>> deleteSlotById(@PathVariable Long slotId,
                        Authentication auth) throws RuntimeException {
                try {
                        String doctorUsername = auth.getName();
                        User doctor = userRepository.findByUsername(doctorUsername).orElseThrow(
                                        () -> new UsernameNotFoundException("Doctor with username '"
                                                        + doctorUsername + "' not found"));
                        Long doctorId = doctor.getDoctorId();

                        Long doctor_id = slotService.getSlotById(slotId).getDoctor().getDoctor_id();

                        if (!doctorId.equals(doctor_id)) {
                                return ResponseEntity.status(403).body(Map.of("success", false,
                                                "message",
                                                "You are not authorized to create slots for another doctor"));
                        }

                        slotService.deleteSlotById(slotId);

                        return ResponseEntity.badRequest().body(Map.of("success", true, "message",
                                        "Deleted the slot with slotId " + slotId));
                } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseEntity.badRequest().body(Map.of("success", false, "message",
                                        "Error deleting slot: " + e.getMessage()));
                }
        }

        @GetMapping("/slots")
        public ResponseEntity<Map<String, Object>> getSlots(Authentication auth) {
                Map<String, Object> response = new HashMap<>();

                try {
                        // Extract the currently logged-in doctor's username
                        String doctorUsername = auth.getName();

                        // Fetch the doctor user entity
                        User doctor = userRepository.findByUsername(doctorUsername).orElseThrow(
                                        () -> new UsernameNotFoundException("Doctor with username '"
                                                        + doctorUsername + "' not found"));

                        Long doctorId = doctor.getDoctorId();

                        // Fetch all available slots for this doctor
                        List<Slot> availableSlots = slotService.getAvailableSlotsByDoctor(doctorId);

                        if (availableSlots.isEmpty()) {
                                response.put("message",
                                                "No available slots found for this doctor.");
                                response.put("slots", Collections.emptyList());
                                return ResponseEntity.ok(response);
                        }

                        // Return successful response
                        response.put("message", "Available slots fetched successfully.");
                        response.put("totalSlots", availableSlots.size());
                        response.put("slots", availableSlots);

                        return ResponseEntity.ok(response);

                } catch (UsernameNotFoundException e) {
                        response.put("error", e.getMessage());
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

                } catch (Exception e) {
                        response.put("error", "An unexpected error occurred: " + e.getMessage());
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(response);
                }
        }

        // complete this slot controller
        // @Autowired
        // private DoctorService doctorService;
        // // public end points
        // @GetMapping("/")
        // public ResponseEntity<List<Doctor>> getAllDoctors() {
        // List<Doctor> doctors = doctorService.getAllDoctors();
        // return ResponseEntity.ok(doctors);
        // }

        // @GetMapping("/{id}")
        // public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        // Doctor doctor = doctorService.getDoctorById(id);
        // if (doctor != null) {
        // return ResponseEntity.ok(doctor);
        // }
        // return ResponseEntity.notFound().build();
        // }

        // @GetMapping("/specialization/{specialization}")
        // public ResponseEntity<List<Doctor>> getDoctorsBySpecialization(@PathVariable String
        // specialization) {
        // List<Doctor> doctors = doctorService.getDoctorsBySpecialization(specialization);
        // return ResponseEntity.ok(doctors);
        // }



        // // Admin End points
        // @PostMapping("/create")
        // public ResponseEntity<String> createDoctor(@RequestBody Doctor doctor){
        // try {
        // doctorService.createDoctor(doctor);
        // return ResponseEntity.ok("Doctor created successfully");
        // } catch (Exception e) {
        // return ResponseEntity.badRequest().body("Error creating doctor: " + e.getMessage());
        // }
        // }

        // // ADDED: Bulk create endpoint to add multiple doctors in one request (JSON array)
        // @PostMapping("/bulk")
        // public ResponseEntity<String> createDoctors(@RequestBody List<Doctor> doctors){
        // try {
        // int saved = doctorService.createDoctors(doctors);
        // return ResponseEntity.ok("Doctors created successfully: " + saved);
        // } catch (Exception e) {
        // return ResponseEntity.badRequest().body("Error creating doctors: " + e.getMessage());
        // }
        // }



}

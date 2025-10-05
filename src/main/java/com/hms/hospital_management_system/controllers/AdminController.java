package com.hms.hospital_management_system.controllers;

import com.hms.hospital_management_system.models.*;
import com.hms.hospital_management_system.services.AppointmentService;
import com.hms.hospital_management_system.services.DoctorService;
import com.hms.hospital_management_system.services.FeedbackService;
import com.hms.hospital_management_system.services.StaffService; // StaffService moved up
import com.hms.hospital_management_system.dto.RegisterDoctorRequest;
import com.hms.hospital_management_system.dto.RegisterStaffRequest;
import com.hms.hospital_management_system.jpaRepository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;


@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DoctorService doctorService;
    private final StaffService staffService;
    private final FeedbackService feedbackService;
    private final AppointmentService appointmentService;


    // Constructor for Dependency Injection (Autowired on fields is discouraged)
    public AdminController(UserRepository userRepository, PasswordEncoder passwordEncoder,
            DoctorService doctorService, StaffService staffService, FeedbackService feedbackService,
            AppointmentService appointmentService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.doctorService = doctorService;
        this.staffService = staffService;
        this.feedbackService = feedbackService;
        this.appointmentService = appointmentService;
    }


    // USER MANAGEMENT
    // -------------------------------------------------------------------------

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @Transactional
    @PostMapping("/register-doctor")
    public ResponseEntity<?> registerDoctor(@RequestBody RegisterDoctorRequest request) {
        try {
            User user = request.getUser();
            Doctor doctor = request.getDoctor();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(Role.DOCTOR);

            // Step 1: Create doctor
            try {
                doctor = doctorService.createDoctor(doctor);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Error creating doctor: " + e.getMessage(), e);
            }

            user.setDoctorId(doctor.getDoctor_id());

            // Step 2: Save user
            try {
                userRepository.save(user);
            } catch (DataIntegrityViolationException e) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Registration failed: Username or email already exists.", e);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Error saving user: " + e.getMessage(), e);
            }

            return ResponseEntity.ok("Doctor registered successfully");

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred during doctor registration: " + e.getMessage(),
                    e);
        }
    }


    @Transactional
    @PostMapping("/register-staff")
    public ResponseEntity<?> registerStaff(@RequestBody RegisterStaffRequest req) {
        User user = req.getUser();
        Staff staff = req.getStaff();
        try {
            // Encode password and set role
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(Role.STAFF);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error encoding password or setting role: " + e.getMessage(), e);
        }

        try {
            // Step 1: Create staff first
            staff = staffService.createStaff(staff);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Staff registration failed: Email or phone already exists.", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error creating staff: " + e.getMessage(), e);
        }

        try {
            // Step 2: Link user to staff and save
            user.setStaffId(staff.getStaffId());
            userRepository.save(user);
            // return ResponseEntity.ok(staff);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "User registration failed: Username already exists.", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error saving user: " + e.getMessage(), e);
        }

        return ResponseEntity.ok(Map.of(
            "message" ,"Staff registered successfully",
            "staff" , staff
        ));
    }


    // STAFF MANAGEMENT
    // -------------------------------------------------------------------------

    // 1. Get staff by ID
    // Renamed to /staff/{id} to use the most specific path (removed the duplicate)
    @GetMapping("/staff/{id}")
    public ResponseEntity<Staff> getStaffById(@PathVariable Long id) {
        Staff staff = staffService.getStaffById(id);
        if (staff != null) {
            return ResponseEntity.ok(staff);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 2. Get all staff
    // Renamed to /staff/all to use the most specific path (removed the duplicate)
    @GetMapping("/staff/all")
    public ResponseEntity<List<Staff>> getAllStaff() {
        List<Staff> staffList = staffService.getAllStaff();
        return ResponseEntity.ok(staffList);
    }

    // 3. Update staff
    // Renamed to /staff/update/{id} for consistency (removed the duplicate)
    @PutMapping("/staff/update/{id}")
    public ResponseEntity<Staff> updateStaff(@PathVariable Long id, @RequestBody Staff staff) {
        Staff updatedStaff = staffService.updateStaff(id, staff);
        return ResponseEntity.ok(updatedStaff);
    }

    // 4. Delete staff
    // Renamed to /staff/delete/{id} for consistency (removed the duplicate)
    @DeleteMapping("/staff/delete/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        try {
            staffService.deleteStaff(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // Should catch more specific exceptions for proper error handling
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 5. Get staff by phone number
    @GetMapping("/staff/phone/{phoneNumber}")
    public ResponseEntity<Staff> getStaffByPhoneNumber(@PathVariable String phoneNumber) {
        Staff staff = staffService.getStaffByPhoneNumber(phoneNumber);
        if (staff != null) {
            return ResponseEntity.ok(staff);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 6. Get staff who joined after a specific date
    // Kept original path and fixed method name conflict
    @GetMapping("/staff/joinedAfter/{date}")
    public ResponseEntity<List<Staff>> getStaffByDateOfJoiningAfter(@PathVariable String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            List<Staff> staffList = staffService.getStaffByDateOfJoiningAfter(localDate);
            return ResponseEntity.ok(staffList);
        } catch (Exception e) {
            // Throwing ResponseStatusException is better than returning
            // ResponseEntity.badRequest().build() here
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid date format. Use YYYY-MM-DD.", e);
        }
    }

    // Feedback Management
    @GetMapping("/feedback/patient/{patientId}")
    public ResponseEntity<List<Feedback>> getFeedbacksByPatientId(@PathVariable Long patientId) {
        List<Feedback> feedbacks = feedbackService.getFeedbacksByPatientId(patientId);
        return ResponseEntity.ok(feedbacks);
    }



    @GetMapping("/feedback/{id}")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable Long id) {
        Feedback feedback = feedbackService.getFeedbackById(id);
        if (feedback != null) {
            return ResponseEntity.ok(feedback);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/feedbacks")
    public ResponseEntity<List<Feedback>> getAllFeedbacks() {
        List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
        return ResponseEntity.ok(feedbacks);
    }

    @DeleteMapping("/delete/feedback/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        try {
            feedbackService.deleteFeedback(id);
            return ResponseEntity.noContent().build();
            // explain what is returned?
            // A ResponseEntity with no content (HTTP status 204) is returned
            // to indicate that the deletion was successful and there is no additional content to
            // send in the response body.
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Apppointment Management
    @GetMapping("/all/appointments/")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/appointment/patient/{patientId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByPatient(
            @PathVariable Long patientId) {
        List<Appointment> appointments = appointmentService.getAppointmentsByPatientId(patientId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long appointmentId) {
        Appointment appointment = appointmentService.getAppointmentById(appointmentId);
        if (appointment != null) {
            return ResponseEntity.ok(appointment);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/cancel/appointment/{appointmentId}")
    public ResponseEntity<Map<String, Object>> cancelAppointment(@PathVariable Long appointmentId) {
        try {
            // can integrate a refund process
            appointmentService.cancelAppointment(appointmentId);
            return ResponseEntity
                    .ok(Map.of("success", true, "message", "Appointment cancelled successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message",
                    "Error cancelling appointment: " + e.getMessage()));
        }
    }

}

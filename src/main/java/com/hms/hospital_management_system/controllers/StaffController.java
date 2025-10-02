package com.hms.hospital_management_system.controllers;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.*;
import com.hms.hospital_management_system.models.Staff;
import com.hms.hospital_management_system.services.StaffService;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @PostMapping("/create")
    public ResponseEntity<Staff> createStaff(@RequestBody Staff staff) {
        Staff createdStaff = staffService.createStaff(staff);
        if (createdStaff != null) {
            return ResponseEntity.ok(createdStaff);
        } else {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Staff> getStaffById(@PathVariable Long id) {
        Staff staff = staffService.getStaffById(id);
        if (staff != null) {
            return ResponseEntity.ok(staff);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Staff>> getAllStaff() {
        List<Staff> staffList = staffService.getAllStaff();
        return ResponseEntity.ok(staffList);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Staff> updateStaff(@PathVariable Long id, @RequestBody Staff staff) {
        Staff updatedStaff = staffService.updateStaff(id, staff);
        return ResponseEntity.ok(updatedStaff);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        try {
            staffService.deleteStaff(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // Get staff by phone number
    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<Staff> getStaffByPhoneNumber(@PathVariable String phoneNumber) {
        Staff staff = staffService.getStaffByPhoneNumber(phoneNumber);
        if (staff != null) {
            return ResponseEntity.ok(staff);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get staff who joined after a specific date
    @GetMapping("/joinedAfter/{date}")
    public ResponseEntity<List<Staff>> getStaffByDateOfJoiningAfter(@PathVariable String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            List<Staff> staffList = staffService.getStaffByDateOfJoiningAfter(localDate);
            return ResponseEntity.ok(staffList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

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

    // @PostMapping("/create")
    // public ResponseEntity<Staff> createStaff(@RequestBody Staff staff) {
    //     Staff createdStaff = staffService.createStaff(staff);
    //     if (createdStaff != null) {
    //         return ResponseEntity.ok(createdStaff);
    //     } else {
    //         return ResponseEntity.status(500).build();
    //     }
    // }

    
}

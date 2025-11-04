package com.hms.hospital_management_system.controllers;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hms.hospital_management_system.services.LabTestService;
import org.springframework.http.ResponseEntity;
import java.util.List;
import com.hms.hospital_management_system.models.LabTest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
@RestController
@RequestMapping("/api/staff/labtest")
public class LabTestController {
    
    @Autowired
    private LabTestService labTestService;

    @GetMapping("/")
    public ResponseEntity<List<LabTest>> testLabTestController() {
        return ResponseEntity.ok(labTestService.getAllLabTests());
    }

    @GetMapping("/testName/{testName}/testType/{testType}")
    public ResponseEntity<?> getLabTestByNameAndType(@PathVariable String testName, @PathVariable String testType) {
        try {
            LabTest labTest = labTestService.getLabTestByNameAndType(testName, testType);
            if (labTest != null) {
                return ResponseEntity.ok(labTest);
            } else {
                return ResponseEntity.status(404).body("Lab test not found");   
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving lab test: " + e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createLabTest(@RequestBody LabTest labTest) {
        
        return ResponseEntity.ok(labTestService.createLabTest(labTest));
    }

    @DeleteMapping("/delete/{testId}")
    public ResponseEntity<?> deleteLabTestById(@PathVariable Long testId) {
        Boolean f = labTestService.deleteLabTestById(testId);
        if (f) {
            return ResponseEntity.ok("Lab test deleted successfully");
        } else {
            return ResponseEntity.status(500).body("Error deleting lab test");
        } 
    }
}

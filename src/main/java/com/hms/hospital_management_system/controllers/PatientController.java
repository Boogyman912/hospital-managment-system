package com.hms.hospital_management_system.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hms.hospital_management_system.models.Patient;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.hms.hospital_management_system.services.PatientService;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    
    @Autowired
    private PatientService patientService;

    @GetMapping("/")
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    @PostMapping("/create")
    public void createPatient(@RequestBody Patient patient){

        patientService.createPatient(patient);
        // return "Create a new patient";
    }


}

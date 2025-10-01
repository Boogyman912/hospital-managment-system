package com.hms.hospital_management_system.services;
import com.hms.hospital_management_system.models.Patient;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hms.hospital_management_system.jpaRepository.PatientRepository;
//In service layer we write the business logic
//@Service annotation is used to mark the class as a service provider
// It is a specialization of the @Component annotation
// It is used to indicate that the class provides some business functionalities
// It is used to separate the business logic from the controller layer
// It interfaces with the repository layer to fetch and save data
@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;
    
    public void createPatient(Patient patient) {
        // logic to create a new patient
        try {
            patientRepository.save(patient);
           System.out.println("Successfully created a new patient!!!");
        } catch (Exception e) {
            System.out.println( "Error: " + e.getMessage());
            throw e;
        }
    }

    public Patient getPatientById(Long id) {
        try {
            return patientRepository.findById(id).orElse(null);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }


    public List<Patient> getAllPatients() {
        // logic to get all patients
        try {
           
            List<Patient> patients = patientRepository.findAll();
            System.out.println("Get all patients");
            return patients;
        }catch(Exception e) {
            System.out.println( "Error: " + e.getMessage());
            return null;
        }
        
    }
}

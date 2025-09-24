package com.hms.hospital_management_system.services;
import com.hms.hospital_management_system.models.Doctor;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DoctorService {
    
    public List<Doctor> getAllDoctors() {
        // logic to get all doctors
        try {

           System.out.println("Get all doctors");
        }catch(Exception e) {
            System.out.println( "Error: " + e.getMessage());
        }
        return null;
    }

}

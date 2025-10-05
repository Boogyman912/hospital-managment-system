package com.hms.hospital_management_system.services;
import com.hms.hospital_management_system.jpaRepository.DoctorRepository;
import com.hms.hospital_management_system.models.Doctor;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorService {
    
    @Autowired
    private DoctorRepository doctorRepository;
    public List<Doctor> getAllDoctors() {
        // logic to get all doctors
        try {
           System.out.println("Getting all doctors");
           return doctorRepository.findAll();
        }catch(Exception e) {
            System.out.println( "Error: " + e.getMessage());
        }
        return null;
    }

    public Doctor createDoctor(Doctor doctor)  throws Exception{
        // logic to create a new doctor
        try {
            System.out.println("Successfully created a new doctor!!!");
            return doctorRepository.save(doctor);
           
        } catch (Exception e) {
            System.out.println( "Error: " + e.getMessage());
            throw e;
        }
    }

    // ADDED: Bulk create doctors with simple counter of successfully saved entries
    public int createDoctors(List<Doctor> doctors) {
        int count = 0;
        for (Doctor doctor : doctors) {
            try {
                doctorRepository.save(doctor);
                count++;
            } catch (Exception e) {
                // continue saving others; log and skip invalid entries
                System.out.println("Error saving doctor (skipped): " + e.getMessage());
            }
        }
        return count;
    }

    public Doctor getDoctorById(Long id) {
        try {
            return doctorRepository.findById(id).orElse(null);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        try {
            return doctorRepository.findBySpecialization(specialization);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

}

package com.hms.hospital_management_system.services;
import com.hms.hospital_management_system.jpaRepository.DoctorRepository;
import com.hms.hospital_management_system.jpaRepository.UserRepository;
import com.hms.hospital_management_system.models.Doctor;
import com.hms.hospital_management_system.models.User;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DoctorService {
    
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;
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
    @Transactional
    public void deleteDoctor(Long doctorId) {
    try {
        // Fetch the user associated with the doctor
        User user = userRepository.findByDoctorId(doctorId).orElseThrow(()-> new UsernameNotFoundException("User doesn't Exist"));
        if (user == null) {
            throw new UsernameNotFoundException("No user found for doctor ID: " + doctorId);
        }

        // Delete the user record first (if required by DB constraints)
        userRepository.deleteById(user.getId());

        // Delete the doctor record
        doctorRepository.deleteById(doctorId);

    } catch (UsernameNotFoundException e) {
        throw e; // rethrow specific error to handle in controller
    } catch (Exception e) {
        throw new RuntimeException("Error deleting doctor with ID: " + doctorId + " — " + e.getMessage(), e);
    }
}

}

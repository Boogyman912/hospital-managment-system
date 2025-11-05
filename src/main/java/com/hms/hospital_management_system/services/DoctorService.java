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
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public Doctor createDoctor(Doctor doctor) throws Exception {
        // logic to create a new doctor
        try {
            System.out.println("Successfully created a new doctor!!!");
            return doctorRepository.save(doctor);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw e;
        }
    }

    // Optimized: Bulk create doctors using saveAll for better performance
    // Note: This uses batch insert which is more efficient but has all-or-nothing semantics.
    // If any doctor fails validation, the entire batch will fail (unlike the previous one-by-one approach).
    @Transactional
    public int createDoctors(List<Doctor> doctors) {
        if (doctors == null || doctors.isEmpty()) {
            return 0;
        }
        try {
            // Use saveAll for batch insert which is more efficient than saving one by one
            List<Doctor> savedDoctors = doctorRepository.saveAll(doctors);
            return savedDoctors.size();
        } catch (Exception e) {
            System.out.println("Error saving doctors in bulk: " + e.getMessage());
            return 0;
        }
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
            User user = userRepository.findByDoctorId(doctorId)
                    .orElseThrow(() -> new UsernameNotFoundException("User doesn't Exist"));
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
            throw new RuntimeException(
                    "Error deleting doctor with ID: " + doctorId + " — " + e.getMessage(), e);
        }
    }

    public List<String> getAllSpecializations() {
        try {
            return doctorRepository.findDistinctSpecializations();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

}

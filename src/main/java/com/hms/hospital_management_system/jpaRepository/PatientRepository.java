package com.hms.hospital_management_system.jpaRepository;

import com.hms.hospital_management_system.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Must be imported for @Query
import org.springframework.data.repository.query.Param; // Must be imported for @Param
import org.springframework.stereotype.Repository;

import java.util.Optional; // Must be imported

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // Fix 1: JPQL entity name should be 'Patient' (uppercase, matching the class)
    // Fix 2: @Param must specify the parameter name in parentheses: @Param("phoneNumber")
    @Query("SELECT p FROM Patient p WHERE p.phoneNumber = :phoneNumber")
    public Optional<Patient> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

}
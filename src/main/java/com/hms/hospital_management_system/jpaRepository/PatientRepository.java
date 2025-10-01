package com.hms.hospital_management_system.jpaRepository;
import com.hms.hospital_management_system.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {


}

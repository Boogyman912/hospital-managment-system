package com.hms.hospital_management_system.jpaRepository;
import com.hms.hospital_management_system.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    
    List<Doctor> findBySpecialization(String specialization);
    List<Doctor> findByActiveTrue();
}

package com.hms.hospital_management_system.jpaRepository;
import org.springframework.stereotype.Repository;
import com.hms.hospital_management_system.models.EmergencyContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
@Repository
public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Long> {

    @Query("SELECT e FROM EmergencyContact e WHERE e.patient.patientId = :patientId")
    List<EmergencyContact> findByPatientId(Long patientId);
}

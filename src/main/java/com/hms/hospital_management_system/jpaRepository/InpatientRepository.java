package com.hms.hospital_management_system.jpaRepository;
import org.springframework.stereotype.Repository;
import com.hms.hospital_management_system.models.Inpatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
@Repository
public interface InpatientRepository extends JpaRepository<Inpatient, Long> {

    // finding inpatient by patient id and currently admitted
    @Query("SELECT i FROM Inpatient i WHERE i.patient.patientId = :patientId AND i.dischargeDate IS NULL")
    Inpatient findByPatientIdAndCurrentlyAdmitted(Long patientId);

    // finding inpatient by currently admitted
    @Query("SELECT i FROM Inpatient i WHERE i.dischargeDate IS NULL")
    Inpatient findByCurrentlyAdmitted();

    // finding inpatient by room id and currently admitted
    @Query("SELECT i FROM Inpatient i WHERE i.room.roomId = :roomId AND i.dischargeDate IS NULL")
    Inpatient findByRoomIdAndCurrentlyAdmitted(Long roomId);

    // finding inpatient admitted on a specific date
    @Query("SELECT i FROM Inpatient i WHERE i.admissionDate = :admissionDate AND i.dischargeDate IS NULL")
    Inpatient findByAdmissionDateAndCurrentlyAdmitted(java.time.LocalDate admissionDate);

    //finding inpatient by discharge date
    @Query("SELECT i FROM Inpatient i WHERE i.dischargeDate = :dischargeDate")
    Inpatient findByDischargeDate(java.time.LocalDate dischargeDate);

    //finding inpatient admitted on and after a specific date
    @Query("SELECT i FROM Inpatient i WHERE i.admissionDate >= :admissionDate AND i.dischargeDate IS NULL")
    Inpatient findByAdmissionDateAfterAndCurrentlyAdmitted(java.time.LocalDate admissionDate);  

    //finding inpatient discharged on and before a specific date
    @Query("SELECT i FROM Inpatient i WHERE i.dischargedDate <= :dischargeDate")
    Inpatient findByDischargeDateBefore(java.time.LocalDate dischargeDate);
    
    @Query("SELECT i FROM Inpatient i WHERE i.room.roomId = :roomId")
    List<Inpatient> findByRoomId(Long roomId);

    @Query("SELECT i FROM Inpatient i WHERE i.patient.patientId = :patientId")
    List<Inpatient> findByPatientId(Long patientId);  
    
    //select those who have discharged but not yet billed
    @Query("SELECT i FROM Inpatient i WHERE i.dischargeDate IS NOT NULL AND i.isBilled = false")
    List<Inpatient> findDischargedButNotBilled();

    
    
}

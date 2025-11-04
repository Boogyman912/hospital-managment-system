package com.hms.hospital_management_system.jpaRepository;

import com.hms.hospital_management_system.models.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    // Use Java field names (doctor, patient, dateIssued) instead of column names
    @Query("SELECT p FROM Prescription p WHERE p.doctor.doctor_id = :doctorId AND p.patient.patientId = :patientId")
    List<Prescription> findByDoctorAndPatient(@Param("doctorId") Long doctorId, @Param("patientId") Long patientId);

    @Query("SELECT p FROM Prescription p WHERE p.doctor.doctor_id = :doctorId AND p.patient.patientId = :patientId AND p.dateIssued = :dateIssued")
    List<Prescription> findByDoctorAndPatientAndDateIssued(@Param("doctorId") Long doctorId, @Param("patientId") Long patientId, @Param("dateIssued") LocalDate dateIssued);

    @Query("SELECT p FROM Prescription p WHERE p.doctor.doctor_id = :doctorId")
    List<Prescription> findByDoctor(@Param("doctorId") Long doctorId);

    @Query("SELECT p FROM Prescription p WHERE p.patient.patientId = :patientId")
    List<Prescription> findByPatient(@Param("patientId") Long patientId);

    @Modifying
    @Query("UPDATE Prescription p SET p.medications = :medications, p.labTests = :labTests, p.instructions = :instructions, p.dateIssued = :dateIssued WHERE p.prescriptionId = :prescriptionId")
    void updatePrescription(@Param("prescriptionId") Long prescriptionId,
                            @Param("medications") List<String> medications,
                            @Param("labTests") List<String> labTests,
                            @Param("instructions") String instructions,
                            @Param("dateIssued") LocalDate dateIssued);

   

    @Query("SELECT p FROM Prescription p")
    List<Prescription> findAllPrescriptions();
}

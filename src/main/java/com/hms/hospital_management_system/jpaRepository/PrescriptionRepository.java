package com.hms.hospital_management_system.jpaRepository;
import com.hms.hospital_management_system.models.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;


@Repository

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    @Query("SELECT p FROM Prescription p WHERE p.doctor.doctor_id = :doctor_id AND p.patient.patient_id = :patient_id")
    List<Prescription> findByDoctorAndPatient(Long doctor_id, Long patient_id);
   
    //query to get prescription by doctor_id and patient_id and date_issued
    @Query("SELECT p FROM Prescription p WHERE p.doctor.doctor_id = :doctor_id AND p.patient.patient_id = :patient_id AND p.date_issued = :date_issued")
    List<Prescription> findByDoctorAndPatientAndDateIssued(Long doctor_id, Long patient_id, LocalDate date_issued);

    //query to get all the prescriptions of a doctor
    @Query("SELECT p FROM Prescription p WHERE p.doctor.doctor_id = :doctor_id")
    List<Prescription> findByDoctor(Long doctor_id);

    //query to get all the prescriptions of a patient
    @Query("SELECT p FROM Prescription p WHERE p.patient.patient_id = :patient_id")
    List<Prescription> findByPatient(Long patient_id);

    @Modifying
    @Query("update Prescription p set p.medications = :medications, p.labTests = :labTests, p.instructions = :instructions, p.date_issued = :date_issued where p.prescription_id = :prescription_id")
    void updatePrescription(Long prescription_id, List<String> medications, List<String> labTests, String instructions, LocalDate date_issued);

    @Modifying
    @Query("delete from Prescription where prescription_id = :prescription_id")
    void deletePrescription(Long prescription_id);

    @Query("select * from Prescription")
    List<Prescription> findAllPrescriptions();
}

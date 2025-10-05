package com.hms.hospital_management_system.jpaRepository;
import com.hms.hospital_management_system.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    // CHANGED: Use explicit JPQL to filter by nested Doctor field named doctor_id
    @Query("SELECT a FROM Appointment a WHERE a.doctor.doctor_id = :doctor_id")
    List<Appointment> findByDoctorId(@Param("doctor_id") Long doctor_id);

    // CHANGED: Use explicit JPQL to filter by nested Patient field named patientId
    @Query("SELECT a FROM Appointment a WHERE a.patient.patientId = :patientId")
    List<Appointment> findByPatientPatientId(@Param("patientId") Long patientId);

    @Query("select a from Appointment a where a.patient.phoneNumber = :phoneNumber and a.appointmentStatus = AppointmentStatus.BOOKED")
    List<Appointment> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);
}

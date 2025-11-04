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
    // Added JOIN FETCH to avoid N+1 query issues
    @Query("SELECT a FROM Appointment a LEFT JOIN FETCH a.doctor LEFT JOIN FETCH a.patient LEFT JOIN FETCH a.slot WHERE a.doctor.doctor_id = :doctor_id")
    List<Appointment> findByDoctorId(@Param("doctor_id") Long doctor_id);

    // CHANGED: Use explicit JPQL to filter by nested Patient field named patientId
    // Added JOIN FETCH to avoid N+1 query issues
    @Query("SELECT a FROM Appointment a LEFT JOIN FETCH a.doctor LEFT JOIN FETCH a.patient LEFT JOIN FETCH a.slot WHERE a.patient.patientId = :patientId AND a.appointmentStatus = com.hms.hospital_management_system.models.Appointment.AppointmentStatus.BOOKED")
    List<Appointment> findByPatientPatientId(@Param("patientId") Long patientId);

    // Added JOIN FETCH to avoid N+1 query issues
    @Query("SELECT a FROM Appointment a LEFT JOIN FETCH a.doctor LEFT JOIN FETCH a.patient LEFT JOIN FETCH a.slot WHERE a.patient.phoneNumber = :phoneNumber AND a.appointmentStatus = com.hms.hospital_management_system.models.Appointment.AppointmentStatus.BOOKED")
    List<Appointment> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

}

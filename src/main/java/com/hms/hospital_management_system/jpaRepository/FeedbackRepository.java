package com.hms.hospital_management_system.jpaRepository;
import com.hms.hospital_management_system.dto.FeedbackDTO;
import com.hms.hospital_management_system.models.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    //Finding all the feedbacks associated with a particular doctor using doctor_id
    @Query("SELECT new com.hms.hospital_management_system.dto.FeedbackDTO(" +
           "f.feedback_id, f.rating, f.comments, f.submitted_at) " +
           "FROM Feedback f " +
           "LEFT JOIN f.appointment a " +
           "WHERE a.doctor.doctor_id = :doctorId")
    List<FeedbackDTO> findFeedbacksByDoctorId(@Param("doctorId") Long doctorId);

    @Modifying
    @Query("insert into Feedback (appointment, patient, rating, comments) values (:appointment, :patient, :rating, :comments)")
    void addFeedback(Feedback feedback);

    @Modifying
    @Query("delete from Feedback f where f.patientId = :patient_id and f.appointmentId = :appointment_id")
    void deleteFeedbackByPatientIdAndAppointmentId(Long patient_id, Long appointment_id);

    @Query("select f from Feedback f where f.patient.patientId = :patient_id")

    List<Feedback> findByPatientPatientId(Long patient_id);
    
}

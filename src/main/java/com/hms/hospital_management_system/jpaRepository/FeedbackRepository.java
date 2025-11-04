package com.hms.hospital_management_system.jpaRepository;
import com.hms.hospital_management_system.dto.FeedbackDTO;
import com.hms.hospital_management_system.models.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    //Finding all the feedbacks associated with a particular doctor using doctor_id
    @Query("SELECT new com.hms.hospital_management_system.dto.FeedbackDTO(" +
           "f.feedbackId, f.rating, f.comments, f.submittedAt) " +
           "FROM Feedback f " +
           "LEFT JOIN f.appointment a " +
           "WHERE a.doctor.doctor_id = :doctorId")
    List<FeedbackDTO> findFeedbacksByDoctorId(@Param("doctorId") Long doctorId);

    // Calculate average rating for a doctor efficiently using aggregation
    @Query("SELECT AVG(f.rating) FROM Feedback f " +
           "LEFT JOIN f.appointment a " +
           "WHERE a.doctor.doctor_id = :doctorId")
    Double calculateAverageRatingByDoctorId(@Param("doctorId") Long doctorId);


    @Query("select f from Feedback f where f.patient.patientId = :patient_id")

    List<Feedback> findByPatientPatientId(Long patient_id);
    
}

package com.hms.hospital_management_system.services;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hms.hospital_management_system.models.Feedback;
import com.hms.hospital_management_system.dto.FeedbackDTO;
import com.hms.hospital_management_system.jpaRepository.FeedbackRepository;
import jakarta.transaction.Transactional;
//import doctor
import com.hms.hospital_management_system.models.Doctor;
import com.hms.hospital_management_system.jpaRepository.DoctorRepository;
@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    public Feedback getFeedbackById(Long id) {
        try{
            return feedbackRepository.findById(id).orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error fetching feedback by ID: " + e.getMessage());
            return null;
        }
    }

    @Transactional
    public Feedback addFeedback(Feedback feedback) {
        try{
            Long doctorId = feedback.getAppointment().getDoctor().getDoctor_id();
            
            // Save the feedback first
            Feedback savedFeedback = feedbackRepository.save(feedback);
            
            // Calculate new average rating efficiently using database aggregation
            Double newAverageRating = feedbackRepository.calculateAverageRatingByDoctorId(doctorId);
            
            // Update doctor's rating
            Doctor doctor = feedback.getAppointment().getDoctor();
            doctor.setRating(newAverageRating != null ? newAverageRating : 0.0);
            doctorRepository.save(doctor);
            
            return savedFeedback;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error adding feedback: " + e.getMessage());
            return null;
        }
    }
    @Transactional
    public void deleteFeedback(Long id) {
        try{
            Feedback feedback = feedbackRepository.findById(id).orElse(null);
            if (feedback == null) {
                System.out.println("Feedback not found with ID: " + id);
                return;
            }
            Long doctorId = feedback.getAppointment().getDoctor().getDoctor_id();

            // Delete the feedback first
            feedbackRepository.deleteById(id);
            
            // Recalculate average rating efficiently using database aggregation
            Double newAverageRating = feedbackRepository.calculateAverageRatingByDoctorId(doctorId);
            
            // Update doctor's rating
            Doctor doctor = feedback.getAppointment().getDoctor();
            doctor.setRating(newAverageRating != null ? newAverageRating : 0.0);
            doctorRepository.save(doctor);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error deleting feedback: " + e.getMessage());
        }
    }
    public List<Feedback> getFeedbacksByPatientId(Long patientId) {
        try{
            return feedbackRepository.findByPatientPatientId(patientId);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error fetching feedbacks by patient ID: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    public List<FeedbackDTO> getFeedbacksByDoctorId(Long doctorId) {
        try{
            return feedbackRepository.findFeedbacksByDoctorId(doctorId);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error fetching feedbacks by doctor ID: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    
}

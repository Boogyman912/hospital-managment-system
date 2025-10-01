package com.hms.hospital_management_system.services;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hms.hospital_management_system.models.Feedback;
import com.hms.hospital_management_system.dto.FeedbackDTO;
import com.hms.hospital_management_system.jpaRepository.FeedbackRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
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
            Double rating = feedback.getRating();

            //writing logic for calculating new average rating for doctor
            List<Feedback> existingFeedbacks = feedbackRepository.findFeedbacksByDoctorId(doctorId).stream()
                .map(dto -> feedbackRepository.findById(dto.feedbackId()).orElse(null))
                .filter(Objects::nonNull)
                .toList(); 
            double totalRating = existingFeedbacks.stream()
                .mapToDouble(Feedback::getRating)
                .sum();
            totalRating += rating; // include new rating
            int newCount = existingFeedbacks.size() + 1;
            double newAverageRating = totalRating / newCount;
            // Update doctor's rating
            Doctor doctor = feedback.getAppointment().getDoctor();
            doctor.setRating(newAverageRating);
            // Save the updated doctor entity if you have a DoctorRepository (not shown here)
            doctorRepository.save(doctor);
            return feedbackRepository.save(feedback);
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
            Double rating = feedback.getRating();

            //writing logic for calculating new average rating for doctor
            List<Feedback> existingFeedbacks = feedbackRepository.findFeedbacksByDoctorId(doctorId).stream()
                .map(dto -> feedbackRepository.findById(dto.feedbackId()).orElse(null))
                .filter(Objects::nonNull)
                .toList(); 
            double totalRating = existingFeedbacks.stream()
                .mapToDouble(Feedback::getRating)
                .sum();
            totalRating -= rating; // include new rating
            int newCount = existingFeedbacks.size() - 1;
            double newAverageRating;
            if(newCount>0){
                newAverageRating = totalRating / newCount;
            }else{
                newAverageRating = 0.0;
            }
            // Update doctor's rating
            Doctor doctor = feedback.getAppointment().getDoctor();
            doctor.setRating(newAverageRating);
            // Save the updated doctor entity if you have a DoctorRepository (not shown here)
            doctorRepository.save(doctor);
            feedbackRepository.deleteById(id);
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

package com.hms.hospital_management_system.controllers;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.hms.hospital_management_system.models.Feedback;
import com.hms.hospital_management_system.services.FeedbackService;
import com.hms.hospital_management_system.dto.FeedbackDTO;
import java.util.List;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Feedback>> getFeedbacksByPatientId(@PathVariable Long patientId) {
        List<Feedback> feedbacks = feedbackService.getFeedbacksByPatientId(patientId);
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<FeedbackDTO>> getFeedbacksByDoctorId(@PathVariable Long doctorId) {
        List<FeedbackDTO> feedbacks = feedbackService.getFeedbacksByDoctorId(doctorId);
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable Long id) {
        Feedback feedback = feedbackService.getFeedbackById(id);
        if (feedback != null) {
            return ResponseEntity.ok(feedback);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping
    public ResponseEntity<List<Feedback>> getAllFeedbacks() {
        List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
        return ResponseEntity.ok(feedbacks);
    }

    @PostMapping("/create")
    public ResponseEntity<Feedback> addFeedback(@RequestBody Feedback feedback) {
        Feedback createdFeedback = feedbackService.addFeedback(feedback);
        return ResponseEntity.ok(createdFeedback);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        try {
            feedbackService.deleteFeedback(id);
            return ResponseEntity.noContent().build();
            // explain what is returned?
            // A ResponseEntity with no content (HTTP status 204) is returned 
            //  to indicate that the deletion was successful and there is no additional content to send in the response body. 
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
}

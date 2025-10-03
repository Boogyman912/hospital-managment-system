package com.hms.hospital_management_system.dto;
import java.time.LocalDateTime;

public record FeedbackDTO(
    Long feedbackId,
    int rating,
    String comments,
    LocalDateTime submittedAt
) {}

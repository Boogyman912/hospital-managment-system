package com.hms.hospital_management_system.dto;
import java.time.LocalDate;

public record FeedbackDTO(
    Long feedbackId,
    int rating,
    String comments,
    LocalDate submittedAt
) {}

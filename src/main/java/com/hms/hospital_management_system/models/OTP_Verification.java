package com.hms.hospital_management_system.models;
import jakarta.persistence.*;
import java.time.LocalDateTime; 

@Entity
@Table(name = "otp_verifications")
public class OTP_Verification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 15, nullable = false)
    private String phoneNumber;

    @Column(length = 6, nullable = false)
    private String otp;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    public OTP_Verification() {
    }

    public OTP_Verification(String phoneNumber, String otp, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.phoneNumber = phoneNumber;
        this.otp = otp;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
package com.hms.hospital_management_system.models;
import jakarta.persistence.*;
import java.time.LocalDate;

// it is relation between patient and room , so it has foreign keys of both patient and room
// is it still an entity ? yes because it has its own attributes like admission date , discharge date etc
// also it can have its own primary key , like inpatient id
@Entity
@Table(name = "inpatients")
public class Inpatient {


    // this model records stores the patient and room assigned to them
    // along with admission and discharge dates
    //also mention annotations for jpa
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inpatientId;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "admission_date",nullable = false)
    private LocalDate admissionDate;

    // as discharge date can be null initially when patient is admitted

    @Column(name = "discharge_date",nullable = true)
    private LocalDate dischargeDate;

    @Column(name = "total_bill_amount",nullable = true)
    private Double totalBillAmount;

    @Column(name = "is_billed",nullable = false)
    private Boolean isBilled;
    public Inpatient() {
        // default constructor for jpa
    }
    public Inpatient(Patient patient, Room room, LocalDate admissionDate, LocalDate dischargeDate, Double totalBillAmount, Boolean isBilled) {
        this.patient = patient;
        this.room = room;
        this.admissionDate = admissionDate;
        this.dischargeDate = dischargeDate;
        this.totalBillAmount = totalBillAmount;
        this.isBilled = isBilled;
    }

    public Long getInpatientId() {
        return inpatientId;
    }

    public void setInpatientId(Long inpatientId) {
        this.inpatientId = inpatientId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDate getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(LocalDate admissionDate) {
        this.admissionDate = admissionDate;
    }

    public LocalDate getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(LocalDate dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public Double getTotalBillAmount() {
        return totalBillAmount;
    }

    public void setTotalBillAmount(Double totalBillAmount) {
        this.totalBillAmount = totalBillAmount;
    }

    public Boolean getIsBilled() {
        return isBilled;
    }

    public void setIsBilled(Boolean isBilled) {
        this.isBilled = isBilled;
    }


}

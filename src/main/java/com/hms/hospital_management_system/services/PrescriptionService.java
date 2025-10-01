package com.hms.hospital_management_system.services;

import org.springframework.stereotype.Service;
import java.util.List;
import com.hms.hospital_management_system.models.Prescription;
import com.hms.hospital_management_system.jpaRepository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;


@Service
public class PrescriptionService {

    @Autowired 
    private PrescriptionRepository PrescriptionRepository;
    public List<Prescription> getPrescriptionsByDoctorAndPatient(Long doctor_id, Long patient_id) {
        return PrescriptionRepository.findByDoctorAndPatient(doctor_id, patient_id);
    }

    public Prescription createPrescription(Prescription prescription) {
        return PrescriptionRepository.save(prescription);
    }

    public List<Prescription> getPrescriptionsByDoctorAndPatientAndDateIssued(Long doctor_id, Long patient_id, LocalDate date_issued) {
        return PrescriptionRepository.findByDoctorAndPatientAndDateIssued(doctor_id, patient_id, date_issued);
    }

    public List<Prescription> getPrescriptionsByDoctor(Long doctor_id) {
        return PrescriptionRepository.findByDoctor(doctor_id);
    }   

    public List<Prescription> getPrescriptionsByPatient(Long patient_id) {
        return PrescriptionRepository.findByPatient(patient_id);
    }

    public void updatePrescription(Long prescription_id, List<String> medications,List<String> labTests, String instructions, LocalDate date_issued) {
        PrescriptionRepository.updatePrescription(prescription_id, medications, labTests, instructions, date_issued);
    }

    public void deletePrescription(Long prescription_id) {
        PrescriptionRepository.deletePrescription(prescription_id);
    }

    public List<Prescription> getAllPrescriptions() {
        return PrescriptionRepository.findAllPrescriptions();
    }

}

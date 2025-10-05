package com.hms.hospital_management_system.services;

import org.springframework.stereotype.Service;
import java.util.*;
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

    public Prescription updatePrescription(Long prescription_id, List<Map<String,String>> medications, List<Map<String,String>> labTests, String instructions, LocalDate date_issued) {
        Prescription existingPrescription = PrescriptionRepository.findById(prescription_id).orElseThrow(()-> new RuntimeException("Prescription not found"));
        if (existingPrescription == null) {
            return null;
        }
        if (medications != null) {
            existingPrescription.addMedications(medications);
        }
        if (labTests != null) {
            existingPrescription.addLabTests(labTests);
        }
        if (instructions != null) {
            existingPrescription.setInstructions(instructions);
        }
        if (date_issued != null) {
            existingPrescription.setDateIssued(date_issued);
        }
        return PrescriptionRepository.save(existingPrescription);
    }

    public void deletePrescription(Long prescription_id) {
        PrescriptionRepository.deleteById(prescription_id);
    }

    public List<Prescription> getAllPrescriptions() {
        return PrescriptionRepository.findAllPrescriptions();
    }

    public Prescription getPrescriptionById(Long prescription_id) {
        return PrescriptionRepository.findById(prescription_id).orElse(null);
    }

}

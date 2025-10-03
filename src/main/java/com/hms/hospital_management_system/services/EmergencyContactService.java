package com.hms.hospital_management_system.services;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.hms.hospital_management_system.jpaRepository.EmergencyContactRepository;
import com.hms.hospital_management_system.models.EmergencyContact;
import java.util.List;
@Service
public class EmergencyContactService {
    @Autowired
    private EmergencyContactRepository emergencyContactRepository;

    public List<EmergencyContact> getEmergencyContactByPatientId(Long patientId) {
        return emergencyContactRepository.findByPatientId(patientId);
    }

    public EmergencyContact addEmergencyContact(EmergencyContact emergencyContact) {
        return emergencyContactRepository.save(emergencyContact);
    }

    public void deleteEmergencyContact(Long id) {
        emergencyContactRepository.deleteById(id);
    }


}

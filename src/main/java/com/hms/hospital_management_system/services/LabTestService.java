package com.hms.hospital_management_system.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import  com.hms.hospital_management_system.jpaRepository.LabTestRepository;
import org.springframework.stereotype.Service;
import com.hms.hospital_management_system.models.LabTest;
import java.util.List;
@Service
public class LabTestService {

    @Autowired
    private LabTestRepository labTestRepository;

    @Modifying
    public LabTest createLabTest(LabTest labTest) {
        try {
            return labTestRepository.save(labTest);
        } catch (Exception e) {
            // Handle exception or log error
            System.out.println("Error creating lab test: " + e.getMessage());
            return null;
        }
    }

    public LabTest getLabTestByNameAndType(String testName, String testType) {
        try {
            return labTestRepository.findByTestNameAndTestType(testName, testType);
        } catch (Exception e) {
            // Handle exception or log error
            System.out.println("Error retrieving lab test: " + e.getMessage());
            return null;
        }
    }

    @Modifying
    public Boolean deleteLabTestById(Long testId) {
        try {
            labTestRepository.deletebyId(testId);
            return true;
        } catch (Exception e) {
            System.out.println("Error deleting lab test: " + e.getMessage());
            return false;
            // Handle exception or log error
        }
    }

    @Query("SELECT lt FROM LabTest lt")
    public List<LabTest> getAllLabTests() {
        return labTestRepository.findAll();
    }



}

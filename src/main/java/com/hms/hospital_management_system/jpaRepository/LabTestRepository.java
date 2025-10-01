package com.hms.hospital_management_system.jpaRepository;
import com.hms.hospital_management_system.models.LabTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
@Repository
public interface LabTestRepository extends JpaRepository<LabTest, Long> {

    

    @Query("SELECT lt FROM LabTest lt WHERE lt.testName = :testName AND lt.testType = :testType")
    LabTest findByTestNameAndTestType(String testName, String testType);

    @Modifying
    @Query("DELETE lt FROM LabTest lt WHERE lt.testId = :testId")

    void deletebyId(Long testId);



}

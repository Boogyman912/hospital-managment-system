package com.hms.hospital_management_system.jpaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hms.hospital_management_system.models.Staff;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    //do it by using phone number
    @Query("SELECT s FROM Staff s WHERE s.phoneNumber = :phoneNumber")
    Staff findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    // find staff joined on and after a specific date
    @Query("SELECT s FROM Staff s WHERE s.dateOfJoining >= :date")
    List<Staff> findByDateOfJoiningAfter(@Param("date") LocalDate date);


    //modify 
}

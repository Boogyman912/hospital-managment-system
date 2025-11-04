package com.hms.hospital_management_system.jpaRepository;

import com.hms.hospital_management_system.models.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    
    @Query("select r from Receipt r where r.appointment.appointmentId = :appointmentId")
    Receipt findByAppointmentAppointmentId(@Param("appointmentId") Long appointmentId);
}

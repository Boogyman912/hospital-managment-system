package com.hms.hospital_management_system.jpaRepository;

import com.hms.hospital_management_system.models.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    
    Receipt findByAppointmentAppointmentId(Long appointmentId);
}

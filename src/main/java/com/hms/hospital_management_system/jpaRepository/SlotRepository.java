package com.hms.hospital_management_system.jpaRepository;

import com.hms.hospital_management_system.models.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Long> {
    
    // CHANGED: Use explicit JPQL because Doctor field name is doctor_id, which breaks derived queries
    @Query("SELECT s FROM Slot s WHERE s.doctor.doctor_id = :doctor_id AND s.date = :date AND s.status = :status")
    List<Slot> findByDoctorAndDateAndStatus(
        @Param("doctor_id") Long doctor_id,
        @Param("date") LocalDate date,
        @Param("status") Slot.SlotStatus status
    );

    // CHANGED: explicit JPQL for doctor and date
    @Query("SELECT s FROM Slot s WHERE s.doctor.doctor_id = :doctor_id AND s.date = :date")
    List<Slot> findByDoctorAndDate(
        @Param("doctor_id") Long doctor_id,
        @Param("date") LocalDate date
    );

    List<Slot> findByStatus(Slot.SlotStatus status);

    // CHANGED: explicit JPQL for doctor filter
    @Query("SELECT s FROM Slot s WHERE s.doctor.doctor_id = :doctor_id")
    List<Slot> findByDoctor(
        @Param("doctor_id") Long doctor_id
    );
}

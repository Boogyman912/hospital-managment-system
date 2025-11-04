package com.hms.hospital_management_system.jpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.hms.hospital_management_system.models.Billing;
import java.util.List;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {

    // The field names in the query must match the entity's field names.
    // Also, @Modifying @Query is not suitable for INSERT with entities in Spring Data JPA.
    // Instead, use save() from JpaRepository for inserting entities.
    // If you want a custom insert, you must use native query and pass individual parameters.

    // Example using save():
    // BillingRepository extends JpaRepository<Billing, Long>
    // So you can simply call billingRepository.save(billing);

    // If you still want a custom insert (not recommended), use native query:
    /*
    @Modifying
    @Query(
        value = "INSERT INTO billing (prescription_id, amount, billing_date, status) VALUES (:prescriptionId, :amount, :billingDate, :status)",
        nativeQuery = true
    )
    void generateBill(Long prescriptionId, Double amount, Date billingDate, String status);
    */

    @Query("select b from Billing b where b.prescription.patient.patientId = :patientId")
    List<Billing> findBillsByPatientId(Long patientId);

    // it is one to one mapping so it will return only one bill
    @Query("select b from Billing b where b.prescription.prescriptionId = :prescriptionId")
    Billing findBillByPrescriptionId(Long prescriptionId);

    
}

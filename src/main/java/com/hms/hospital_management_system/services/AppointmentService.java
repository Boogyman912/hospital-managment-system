package com.hms.hospital_management_system.services;
import com.hms.hospital_management_system.models.Patient;
import java.util.List;
import org.springframework.stereotype.Service;


@Service
public class AppointmentService {
    
    public List<Patient> getAppointmentById(Long id) {
        // logic to get all the appointments of Doctor with id
        try {

           System.out.println("Get appointment by ID");
        } catch (Exception e) {
            System.out.println( "Error: " + e.getMessage());
        }
        return null;
    }

    public void bookAppointment(Long id) {
        // logic to book an appointment with doctor with id
        try {

           System.out.println("Book an appointment");
        } catch (Exception e) {
            System.out.println( "Error: " + e.getMessage());
        }
    }

}

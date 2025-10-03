package com.hms.hospital_management_system.services;

import org.springframework.stereotype.Service;
import java.util.*;
import com.hms.hospital_management_system.models.Inpatient;
import com.hms.hospital_management_system.jpaRepository.InpatientRepository;
import com.hms.hospital_management_system.jpaRepository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.hms.hospital_management_system.models.Room;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import com.hms.hospital_management_system.jpaRepository.BillingRepository;
import com.hms.hospital_management_system.models.Billing;
import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class InpatientService {

    @Autowired
    private InpatientRepository inpatientRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BillingService billingService;
    @Autowired
    private BillingRepository billingRepository;

    public List<Inpatient> getAllInpatients() {
        return inpatientRepository.findAll();
    }

    public Inpatient getInpatientById(Long id) {
        return inpatientRepository.findById(id).orElse(null);
    }

    public Inpatient admitPatient(Inpatient inpatient) throws RuntimeException {

        try {
            Room room = inpatient.getRoom();
            if (room != null) {
                if (room.getStatus() == Room.RoomStatus.OCCUPIED) {
                    throw new RuntimeException("Room is already occupied");
                } else {
                    room.setStatus(Room.RoomStatus.OCCUPIED);
                    roomRepository.save(room);
                    return inpatientRepository.save(inpatient);
                }
            } else {
                throw new RuntimeException("Room is not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error in creating inpatient: " + e.getMessage());
        }
    }

    @Transactional
    public Inpatient updateInpatientRoom(Long id, Room newRoom) throws RuntimeException {
        try {
            Inpatient existingInpatient = inpatientRepository.findById(id).orElse(null);
            if (existingInpatient == null) {
                throw new RuntimeException("Inpatient not found");
            }
            Room oldRoom = existingInpatient.getRoom();
            if (oldRoom == null) {
                throw new RuntimeException("Old room is not found");
            }

            if (newRoom.equals(oldRoom)) {
                throw new RuntimeException("New room is same as old room");
            }
            // Room newRoom = inpatient.getRoom();
            // while changing room , a new entry will be created in inpatient table with new room
            // and old room will be freed
            if (newRoom.getStatus() == Room.RoomStatus.OCCUPIED) {
                throw new RuntimeException("New room is already occupied");
            }
            // Free the old room
            oldRoom.setStatus(Room.RoomStatus.AVAILABLE);
            roomRepository.save(oldRoom);

            existingInpatient.setDischargeDate(LocalDate.now());
            // calculating total bill for old room , we will take ceiling value of days stayed
            // if patient stayed for 2.3 days, we will charge for 3 days
            // if patient stayed for 2.0 days, we will charge for 2 days
            // write code which take ceil value for the same
            long daysStayed = (long) Math.ceil(DAYS.between(existingInpatient.getAdmissionDate(),
                    existingInpatient.getDischargeDate()));
            if (daysStayed == 0) {
                daysStayed = 1; // minimum 1 day charge
            }
            double totalBill = daysStayed * oldRoom.getPricePerDay();
            existingInpatient.setTotalBillAmount(totalBill);
            existingInpatient.setIsBilled(false); // not billed yet
            inpatientRepository.save(existingInpatient);

            // Create a new inpatient entry for the new room
            Inpatient newInpatient = new Inpatient();
            newInpatient.setPatient(existingInpatient.getPatient());
            newInpatient.setRoom(newRoom);
            newInpatient.setAdmissionDate(LocalDate.now());
            newInpatient.setDischargeDate(null);
            newInpatient.setTotalBillAmount(0.0);
            newInpatient.setIsBilled(false);
            inpatientRepository.save(newInpatient);



            // Occupy the new room
            newRoom.setStatus(Room.RoomStatus.OCCUPIED);
            roomRepository.save(newRoom);



            return inpatientRepository.save(newInpatient);

            // updating

        } catch (Exception e) {
            throw new RuntimeException("Error in updating inpatient: " + e.getMessage());
        }
    }

    public void deleteInpatient(Long id) {
        inpatientRepository.deleteById(id);
    }

    public List<Inpatient> getInpatientsByPatientId(Long patientId) {
        return inpatientRepository.findByPatientId(patientId);
    }

    // dicharging a patient
    @Transactional
    public Inpatient dischargeInpatient(Long id) throws RuntimeException {
        try {
            Inpatient inpatient = inpatientRepository.findById(id).orElse(null);
            if (inpatient == null) {
                throw new RuntimeException("Inpatient not found");
            }
            if (inpatient.getDischargeDate() != null) {
                throw new RuntimeException("Inpatient already discharged");
            }
            Room room = inpatient.getRoom();
            if (room == null) {
                throw new RuntimeException("Room was not allocated");
            }
            inpatient.setDischargeDate(LocalDate.now());
            // calculating total bill
            long daysStayed = (long) Math.ceil(DAYS.between(inpatient.getAdmissionDate(),
                    inpatient.getDischargeDate()));
            if (daysStayed == 0) {
                daysStayed = 1; // minimum 1 day charge
            }
            double totalBill = daysStayed * inpatient.getRoom().getPricePerDay();
            inpatient.setTotalBillAmount(totalBill);
            inpatient.setIsBilled(false); // not billed yet
            // Free the room
            
            
                room.setStatus(Room.RoomStatus.AVAILABLE);
                roomRepository.save(room);
            return inpatientRepository.save(inpatient);
        } catch (Exception e) {
            throw new RuntimeException("Error in discharging inpatient: " + e.getMessage());
        }
    }

    public Billing generateInpatientBillForPatient(Long patientId) throws RuntimeException{
        try{
            List<Inpatient> inpatients = inpatientRepository.findByPatientId(patientId);
            List<Inpatient> unPaidInpatients = new ArrayList<>();

            // iterate through inpatients
            for (Inpatient inpatient : inpatients) {
                if(!inpatient.getIsBilled()){
                    unPaidInpatients.add(inpatient);
                }
            }

            if(unPaidInpatients.isEmpty()){
                throw new RuntimeException("Patient paid all his room bills");
            }

            Billing inpatientBill = billingService.generateInpatientBill(unPaidInpatients);

            return billingRepository.save(inpatientBill);
        } catch(Exception e){
            throw new RuntimeException("Error in generating bill for patient: " + e.getMessage());
        }
    }

    public List<Inpatient> getInpatientsByRoomId(Long roomId) {
        return inpatientRepository.findByRoomId(roomId);
    }
}

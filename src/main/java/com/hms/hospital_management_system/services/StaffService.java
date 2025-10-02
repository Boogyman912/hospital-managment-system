package com.hms.hospital_management_system.services;
import org.springframework.stereotype.Service;
import java.util.*;
import com.hms.hospital_management_system.models.Staff;
import com.hms.hospital_management_system.jpaRepository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;
@Service
public class StaffService {

    @Autowired 
    private StaffRepository staffRepository;

    public Staff createStaff(Staff staff) {
        try {
            return staffRepository.save(staff);
        } catch (Exception e) {
            // You can log the exception here if needed
            return null;
        }
    }


    public Staff getStaffById(Long id) {
        return staffRepository.findById(id).orElse(null);
    }
    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }
    public Staff updateStaff(Long id, Staff staff) {
        return staffRepository.save(staff);
    }
    public void deleteStaff(Long id) {
        staffRepository.deleteById(id);
    }

    // do by phone number
    public Staff getStaffByPhoneNumber(String phoneNumber) {
        return staffRepository.findByPhoneNumber(phoneNumber);
    }

    public List<Staff> getStaffByDateOfJoiningAfter(LocalDate date) {
        return staffRepository.findByDateOfJoiningAfter(date);
    }

}

package com.hms.hospital_management_system.dto;

import com.hms.hospital_management_system.models.User;
import com.hms.hospital_management_system.models.Staff;
public class RegisterStaffRequest {

    private User user;
    private Staff staff;

    public User getUser(){
        return this.user;
    }
    public Staff getStaff(){
        return this.staff;
    }

    public void setUser(User user){
        this.user = user;
    }
    public void setStaff( Staff staff){
        this.staff = staff;
    }
}

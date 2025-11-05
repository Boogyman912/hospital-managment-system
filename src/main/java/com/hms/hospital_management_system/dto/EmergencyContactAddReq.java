package com.hms.hospital_management_system.dto;

public class EmergencyContactAddReq {
   
    private String  patientPhoneNumber;

    private String name;

    private String relation;

    private String phoneNumber;
    public String getPatientPhoneNumber() {
        return patientPhoneNumber;
    }
    public void setPatientPhoneNumber(String patientPhoneNumber) {
        this.patientPhoneNumber = patientPhoneNumber;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getRelation() {
        return relation;
    }
    public void setRelation(String relation) {
        this.relation = relation;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }


}

package com.shakticoin.app.api.user;

import java.util.List;

public class CreateUserParameters {
    private String first_name;
    private String last_name;
    private String middle_name;
    private String date_of_birth;
    private String password;
    private String email;
    private String mobile;
    private String occupation;
    private String highest_level_of_education;
    private List<Citizenship> citizenship;
    private List<Residence> residence;
    private List<Kinship> kinship;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String firstName) {
        this.first_name = firstName;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String lastName) {
        this.last_name = lastName;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middleName) {
        middle_name = middleName;
    }

    public String getDate_of_birth(String date_of_birth) {
        return date_of_birth;
    }

    public void setDate_of_birth(String dateOfBirth) {
        this.date_of_birth = dateOfBirth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String phoneNumber) {
        this.mobile = phoneNumber;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getHighest_level_of_education() {
        return highest_level_of_education;
    }

    public void setHighest_level_of_education(String highestLevelOfEducation) {
        this.highest_level_of_education = highestLevelOfEducation;
    }

    public List<Citizenship> getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(List<Citizenship> citizenship) {
        this.citizenship = citizenship;
    }

    public List<Residence> getResidence() {
        return residence;
    }

    public void setResidence(List<Residence> residence) {
        this.residence = residence;
    }

    public List<Kinship> getKinship() {
        return kinship;
    }

    public void setKinship(List<Kinship> kinship) {
        this.kinship = kinship;
    }
}

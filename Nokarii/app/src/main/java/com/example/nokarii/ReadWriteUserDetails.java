package com.example.nokarii;

public class ReadWriteUserDetails {

    String doB;
    String gender;
    String mobile;
    String userName;
    String userEmail;
    String profilePicUrl;
    String jobType;
    String profession;
    String salary;
    String qualification;
    String experience;// Change the data type to String for salary

    public ReadWriteUserDetails(String doB, String gender, String mobile, String userName, String userEmail, String profilePicUrl, String jobType, String profession, String salary, String qualification, String experience) {
        this.doB = doB;
        this.gender = gender;
        this.mobile = mobile;
        this.userName = userName;
        this.userEmail = userEmail;
        this.profilePicUrl = profilePicUrl;
        this.jobType = jobType;
        this.profession = profession;
        this.salary = salary;
        this.qualification = qualification;
        this.experience = experience;
    }

    // Default constructor
    public ReadWriteUserDetails() {
    }

    // Getter and Setter methods
    public String getDoB() {
        return doB;
    }

    public void setDoB(String doB) {
        this.doB = doB;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }


    public String getQualification() {
        return  qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }
}

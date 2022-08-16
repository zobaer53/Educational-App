package com.educational_app.model;

public class User {
    private String userName;
    private String userProfile;
    private String userPhone;
    private String userEmail;

    public User(String userName, String userProfile, String userPhone, String userEmail) {
        this.userName = userName;
        this.userProfile = userProfile;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
    }

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}

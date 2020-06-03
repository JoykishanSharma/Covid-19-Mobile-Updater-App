package com.joyappsdevteam.covid_19tracer.authentication_module;

public class UserDetails {

    private String name;
    private String email;
    private String location;
    private String mobile;
    private String userID;

    public UserDetails(String name, String email, String location, String mobile, String userID) {
        this.name = name;
        this.email = email;
        this.location = location;
        this.mobile = mobile;
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}

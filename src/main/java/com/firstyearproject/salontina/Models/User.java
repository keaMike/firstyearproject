package com.firstyearproject.salontina.Models;

import java.util.List;

public class User {

    private int userId;
    private String username;
    private int userPhonenumber;
    private boolean userNewsLetter;
    private String userEmail;
    private String userPreference;
    private List<String> userRoles; //Find out about enums
    private List<Booking> userHistory;

    public User() {
    }

    public User(int userId, String username, int userPhonenumber, boolean userNewsLetter, String userEmail, String userPreference, List<String> userRoles, List<Booking> userHistory) {
        this.userId = userId;
        this.username = username;
        this.userPhonenumber = userPhonenumber;
        this.userNewsLetter = userNewsLetter;
        this.userEmail = userEmail;
        this.userPreference = userPreference;
        this.userRoles = userRoles;
        this.userHistory = userHistory;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserPhonenumber() {
        return userPhonenumber;
    }

    public void setUserPhonenumber(int userPhonenumber) {
        this.userPhonenumber = userPhonenumber;
    }

    public boolean isUserNewsLetter() {
        return userNewsLetter;
    }

    public void setUserNewsLetter(boolean userNewsLetter) {
        this.userNewsLetter = userNewsLetter;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPreference() {
        return userPreference;
    }

    public void setUserPreference(String userPreference) {
        this.userPreference = userPreference;
    }

    public List<String> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<String> userRoles) {
        this.userRoles = userRoles;
    }

    public List<Booking> getUserHistory() {
        return userHistory;
    }

    public void setUserHistory(List<Booking> userHistory) {
        this.userHistory = userHistory;
    }
}

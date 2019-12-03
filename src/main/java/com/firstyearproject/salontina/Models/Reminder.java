package com.firstyearproject.salontina.Models;

import java.util.Date;

public class Reminder {

    private String reminderUsername;
    private String reminderPhonenumber;
    private Date reminderDate;
    private String reminderTime;

    public Reminder() {
    }

    public Reminder(String reminderUsername, String reminderPhonenumber, Date reminderDate, String reminderTime) {
        this.reminderUsername = reminderUsername;
        this.reminderPhonenumber = reminderPhonenumber;
        this.reminderDate = reminderDate;
        this.reminderTime = reminderTime;
    }

    public String getReminderUsername() {
        return reminderUsername;
    }

    public void setReminderUsername(String reminderUsername) {
        this.reminderUsername = reminderUsername;
    }

    public String getReminderPhonenumber() {
        return reminderPhonenumber;
    }

    public void setReminderPhonenumber(String reminderPhonenumber) {
        this.reminderPhonenumber = reminderPhonenumber;
    }

    public Date getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(Date reminderDate) {
        this.reminderDate = reminderDate;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }
}

package com.firstyearproject.salontina.Models;

import java.util.Date;

public class Reminder {

    private String reminderUsername;
    private String reminderPhonenumber;
    private Date reminderDate;

    public Reminder() {
    }

    public Reminder(String reminderUsername, String reminderPhonenumber, Date reminderDate) {
        this.reminderUsername = reminderUsername;
        this.reminderPhonenumber = reminderPhonenumber;
        this.reminderDate = reminderDate;
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
}

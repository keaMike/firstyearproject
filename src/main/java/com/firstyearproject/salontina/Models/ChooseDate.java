package com.firstyearproject.salontina.Models;

import java.sql.Date;

public class ChooseDate {

    private Date date;

    public ChooseDate() {
    }

    public ChooseDate(Date string) {
        this.date = string;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

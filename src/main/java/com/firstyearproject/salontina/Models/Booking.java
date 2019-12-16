package com.firstyearproject.salontina.Models;

import java.sql.Date;
import java.util.List;

public class Booking {

    private int bookingId;
    private int bookingUserId;
    private String bookingTime;
    private Date bookingDate;
    private String bookingComment;
    private List<Treatment> bookingTreatmentList;

    public Booking() {
    }

    public Booking(int id, int userId, String bookingTime, Date date, String comment, List<Treatment> treatmentList) {
        this.bookingId = id;
        this.bookingUserId = userId;
        this.bookingTime = bookingTime;
        this.bookingDate = date;
        this.bookingComment = comment;
        this.bookingTreatmentList = treatmentList;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getBookingUserId() {
        return bookingUserId;
    }

    public void setBookingUserId(int bookingUserId) {
        this.bookingUserId = bookingUserId;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingComment() {
        return bookingComment;
    }

    public void setBookingComment(String bookingComment) {
        this.bookingComment = bookingComment;
    }

    public List<Treatment> getBookingTreatmentList() {
        return bookingTreatmentList;
    }

    public void setBookingTreatmentList(List<Treatment> bookingTreatmentList) {
        this.bookingTreatmentList = bookingTreatmentList;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", bookingUserId=" + bookingUserId +
                ", bookingTime='" + bookingTime + '\'' +
                ", bookingDate=" + bookingDate +
                ", bookingComment='" + bookingComment + '\'' +
                ", bookingTreatmentList=" + bookingTreatmentList +
                '}';
    }
}

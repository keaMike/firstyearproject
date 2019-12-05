package com.firstyearproject.salontina.Services;

import com.firstyearproject.salontina.Models.Booking;

import java.util.List;


public interface BookingService {

    boolean addBooking(Booking booking);
    boolean deleteBooking(int bookingId);
    boolean editBooking(Booking booking);
    List<Booking> getBookingList(String startDate, String endDate);
    List<Booking> getBookingList(String date);
    List<Booking> getBookingList(int userid);
    boolean addVacationDate(String dateString, int userId);
}

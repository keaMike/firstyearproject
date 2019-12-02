package com.firstyearproject.salontina.Services;

import com.firstyearproject.salontina.Models.Booking;

import java.util.Date;
import java.util.List;


public interface BookingService {

    boolean addBooking(Booking booking);
    boolean deleteBooking(int bookingId);
    boolean editBooking(Booking booking);
    List getBookingList(Date startDate, Date endDate);
    List getBookingList(int userid);
}

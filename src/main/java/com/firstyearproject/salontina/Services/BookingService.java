package com.firstyearproject.salontina.Services;

import com.firstyearproject.salontina.Models.Booking;
import com.firstyearproject.salontina.Models.Reminder;

import java.util.List;


public interface BookingService {

    boolean addBooking(Booking booking);
    boolean deleteBooking(List<Booking> bookingList, int bookingId, boolean admin);
    boolean editBooking(Booking booking);
    List<Booking> getBookingList(String startDate, String endDate);
    List<Booking> getBookingList(String date);
    List<Booking> getBookingList(int userid);
    List<Booking> getFutureBookings();
    boolean addVacationDate(String dateString, int userId);
    boolean bookingExists(Booking booking);
}

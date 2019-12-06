package com.firstyearproject.salontina.Repositories;

import com.firstyearproject.salontina.Models.Booking;
import com.firstyearproject.salontina.Models.Treatment;

import java.sql.Date;
import java.util.List;

public interface BookingRepo {
    boolean addBooking(Booking booking);
    boolean addTreatmentsToBooking(List<Treatment> treatmentList, Booking booking);
    List<Booking> findBookingsByUserId(int userid);
    List<Booking> getBookingList(Date startDate, Date endDate);
    List<Booking> getBookingList(Date date);
    List<Booking> getFutureBookings();
    boolean addVacationDate(Date date, int userId);
    boolean deleteBooking(int bookingId);
    boolean deleteTreatmentByBookingId(int bookingId);

}

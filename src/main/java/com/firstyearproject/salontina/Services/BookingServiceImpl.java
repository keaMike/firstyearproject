package com.firstyearproject.salontina.Services;

import com.firstyearproject.salontina.Models.Booking;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService{

    public boolean addBooking(Booking booking){
        return false;
    }

    public boolean deleteBooking(int bookingId){
        return false;
    }

    public boolean editBooking(Booking booking){
        return false;
    }

    public List<Booking> getBookingList(Date startDate, Date endDate){
        return null;
    }

    public List<Booking> getBookingList(int userId){
        return null;
    }

}

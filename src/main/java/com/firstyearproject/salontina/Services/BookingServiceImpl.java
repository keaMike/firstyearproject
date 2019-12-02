package com.firstyearproject.salontina.Services;

import com.firstyearproject.salontina.Models.Booking;
import com.firstyearproject.salontina.Repositories.BookingRepoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService{

    @Autowired
    BookingRepoImpl BR;

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

    //Mike
    public List<Booking> getBookingList(int userId){
        List<Booking> bookings = BR.findBookingsByUserId(userId);
        for(Booking b : bookings) {
            BR.addAllBookingTreatments(b);
        }
        return bookings;
    }

}

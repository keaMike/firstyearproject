package com.firstyearproject.salontina.Handlers;

import com.firstyearproject.salontina.Models.Booking;
import com.firstyearproject.salontina.Models.Treatment;
import com.firstyearproject.salontina.Repositories.BookingRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BookingHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    BookingRepo bookingRepo;

    public boolean addBooking(Booking booking){
        return false;
    }

    public boolean deleteBooking(int bookingId){
        return false;
    }

    public boolean editBooking(Booking booking){
        return false;
    }

    public List<Booking> getAllBookings() {
        return bookingRepo.findAllBookings();
    }

    public List<Booking> getBookingList(Date startDate, Date endDate){
        return null;
    }

    public List<Booking> getBookingList(int userId){
        return null;
    }

    public List<Date> getAvailableDates(List<Treatment> treatments) {
        ArrayList<Date> availableDates = new ArrayList();
        //Current user
        double totalTreatmentTime = 0.5; //getTotalTreatmentTime(treatments);

        //First booking
        double bOneTotalTreatmentTime = 1.5; //getTotalTreatmentTime(bookings.get(i).getBookingTreatmentList());
        int openingTime = 8;
        int closingTime = 17;

        List<Booking> bookings = getAllBookings();
        for(int i = 0; i < bookings.size() - 1; i++) {
            log.info(bookings.get(i).getBookingTime() + "");

            if((bookings.get(i).getBookingTime() + bOneTotalTreatmentTime) -
                    bookings.get(i+1).getBookingDate().getTime() > totalTreatmentTime) {
                availableDates.add(bookings.get(i).getBookingDate());
            }
        }
        return availableDates;
    }

    public double getTotalTreatmentTime(List<Treatment> treatments) {
        int totalTreatmentTime = 0;
        for(int i = 0; i < treatments.size(); i++) {
            totalTreatmentTime =+ treatments.get(i).getTreatmentDuration();
        }
        return totalTreatmentTime;
    }

    public Date getTodaysDate() {
        return null;
    }

    public Date getTomorrowDate() {
        return null;
    }

}

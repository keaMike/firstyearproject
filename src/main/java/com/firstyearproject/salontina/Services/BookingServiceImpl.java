package com.firstyearproject.salontina.Services;

import com.firstyearproject.salontina.Models.Booking;
import com.firstyearproject.salontina.Repositories.BookingRepoImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService{

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    BookingRepoImpl bookingRepo;

    //Luca
    public boolean addBooking(Booking booking){
        log.info("addBooking method started...");
        return bookingRepo.addBooking(booking);
    }

    //Mike
    public boolean deleteBooking(List<Booking> bookingList, int bookingId, boolean admin){
        log.info("deleteBooking method started...");

        if(admin){
            bookingRepo.deleteBooking(bookingId);
            return true;
        }

        for(Booking booking : bookingList) {
            if(booking.getBookingId() == bookingId) {
                bookingRepo.deleteBooking(bookingId);
                return true;
            }
        }
        return false;
    }

    public boolean editBooking(Booking booking){
        log.info("editBooking method started...");
        //TODO
        return false;
    }

    //Luca
    @Override
    public List<Booking> getBookingList(String startDateString, String endDateString){
        log.info("getBookingList(String, String) method started...");
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            java.util.Date startDate = sdf.parse(startDateString);
            java.util.Date endDate = sdf.parse(endDateString);

            return bookingRepo.getBookingList(new Date(startDate.getTime()), new Date(endDate.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Luca
    @Override
    public List<Booking> getBookingList(String dateString){
        log.info("getBookingList(String) method started...");

        List<Booking> bookingList = bookingRepo.getBookingList(parseDateString(dateString));

        if(bookingList == null){
            bookingList = new ArrayList<>();
        }

        for(int i = 0; i <= 7; i++){
            String time = (i + 8) + ":00";
            if(time.length() != 5){
                time = "0" + time;
            }
            if(!timeIsBooked(bookingList, time)){
                bookingList.add(i, new Booking());
                bookingList.get(i).setBookingId(0);
                bookingList.get(i).setBookingTime(time);
                bookingList.get(i).setBookingDate(parseDateString(dateString));
            }
        }

        return bookingList;
    }

    //Luca
    public Date parseDateString(String dateString){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            java.util.Date date = sdf.parse(dateString);

            return new Date(date.getTime());
        } catch (ParseException e){
            e.printStackTrace();
        }
        return null;
    }

    //Luca
    public List<Booking> getFutureBookings(){
        log.info("getFutureBookings method started...");
        return bookingRepo.getFutureBookings();
    }

    //Luca
    private boolean timeIsBooked(List<Booking> bookingList, String time){
        if(bookingList == null){
            return false;
        }
        for(Booking b : bookingList){
            if(b.getBookingTime() != null && b.getBookingTime().equals(time)){
                return true;
            }
        }
        return false;
    }

    //Luca
    @Override
    public boolean addVacationDate(String dateString, int userId){
        log.info("addVacationDate method started...");
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sdf.parse(dateString);

            return bookingRepo.addVacationDate(new Date(date.getTime()), userId);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean bookingExists(Booking booking) {
        return bookingRepo.bookingExists(booking);
    }

    //Mike
    @Override
    public List<Booking> getBookingList(int userId){
        log.info("getBookingList(int) method started...");
        return bookingRepo.findBookingsByUserId(userId);
    }
}

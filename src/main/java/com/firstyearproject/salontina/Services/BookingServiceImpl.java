package com.firstyearproject.salontina.Services;

import com.firstyearproject.salontina.Models.Booking;
import com.firstyearproject.salontina.Repositories.BookingRepoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    //Luca
    public List<Booking> getBookingList(String startDateString, String endDateString){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            java.util.Date startDate = sdf.parse(startDateString);
            java.util.Date endDate = sdf.parse(endDateString);

            return BR.getBookingList(new Date(startDate.getTime()), new Date(endDate.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Luca
    public List<Booking> getBookingList(String dateString){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            java.util.Date date = sdf.parse(dateString);

            List<Booking> bookingList = BR.getBookingList(new Date(date.getTime()));

            for(int i = 0; i <= 7; i++){
                String time = (i + 8) + ":00";
                if(time.length() != 5){
                    time = "0" + time;
                }
                if(!timeIsBooked(bookingList, time)){
                    bookingList.add(i, new Booking());
                    bookingList.get(i).setBookingId(0);
                    bookingList.get(i).setBookingTime(time);
                    bookingList.get(i).setBookingDate(date);
                }
            }

            return bookingList;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Luca
    private boolean timeIsBooked(List<Booking> bookingList, String time){
        for(Booking b : bookingList){
            if(b.getBookingTime() != null && b.getBookingTime().equals(time)){
                return true;
            }
        }
        return false;
    }

    //Mike
    public List<Booking> getBookingList(int userId){
        return BR.findBookingsByUserId(userId);
    }


}

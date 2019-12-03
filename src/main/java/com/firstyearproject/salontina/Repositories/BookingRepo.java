package com.firstyearproject.salontina.Repositories;

import com.firstyearproject.salontina.Models.Booking;
import com.firstyearproject.salontina.Repositories.DbHelper.DbHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class BookingRepo {

    @Autowired
    DbHelper dbHelper;

    Connection con = null;
    PreparedStatement pstm = null;

    public List findAllBookings() {
        try {
            con = dbHelper.createConnection();
            pstm = con.prepareStatement("SELECT * FROM bookings ORDER BY bookings_time ASC");
            ResultSet rs = pstm.executeQuery();
            ArrayList bookings = new ArrayList();
            while(rs.next()) {
                Booking b = new Booking();
                b.setBookingId(rs.getInt(1));
                b.setBookingUserId(rs.getInt(2));
                b.setBookingTime(rs.getString(3));
                b.setBookingDate(rs.getDate(4));
                b.setBookingComment(rs.getString(5));
                bookings.add(b);
            }
            return bookings;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.close();
        }
        return null;
    }
}

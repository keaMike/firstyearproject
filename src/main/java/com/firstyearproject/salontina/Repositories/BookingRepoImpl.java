package com.firstyearproject.salontina.Repositories;

import com.firstyearproject.salontina.Models.Booking;
import com.firstyearproject.salontina.Models.Treatment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookingRepoImpl implements BookingRepo{

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private PreparedStatement pstm;
    private Connection con;

    @Autowired
    MySQLConnector mySQLConnector;


    //Mike
    public List findBookingsByUserId(int userid) {
        try {
            con = mySQLConnector.openConnection();
            pstm = con.prepareStatement("SELECT * FROM bookings WHERE users_id = " + userid);
            ResultSet rs = pstm.executeQuery();
            ArrayList<Booking> bookings = new ArrayList<>();
            while(rs.next()) {
                Booking b = new Booking();
                b.setBookingId(rs.getInt(1));
                b.setBookingUserId(rs.getInt(2));
                b.setBookingDate(rs.getDate(4));
                b.setBookingComment(rs.getString(5));
                bookings.add(b);
            }
            return bookings;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mySQLConnector.closeConnection();
        }
        return null;
    }

    //Mike
    public void addAllBookingTreatments(Booking booking) {
        try {
            con = mySQLConnector.openConnection();
            pstm = con.prepareStatement("SELECT (SELECT treatments_name FROM treatments WHERE treatments_id = bookings_treatment.treatments_id) AS treatments " +
                                                                "FROM bookings_treatment WHERE bookings_id = " + booking.getBookingId());
            ResultSet rs = pstm.executeQuery();
            while(rs.next()) {
                Treatment t = new Treatment();
                t.setProductName(rs.getString(1));
                booking.getBookingTreatmentList().add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mySQLConnector.closeConnection();
        }
    }
}

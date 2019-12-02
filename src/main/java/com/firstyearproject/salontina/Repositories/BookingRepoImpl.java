package com.firstyearproject.salontina.Repositories;

import com.firstyearproject.salontina.Models.Booking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class BookingRepoImpl implements BookingRepo{

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MySQLConnector mySQLConnector;

    public List findBookingsById(int userid) {
        try {
            Connection con = mySQLConnector.openConnection();
            PreparedStatement pstm = con.prepareStatement("SELECT * FROM bookings WHERE users_id = " + userid);
            ResultSet rs = pstm.executeQuery();
            while(rs.next()) {
                Booking b = new Booking();
                b.setBookingId(rs.getInt(1));
                b.setBookingUserId(rs.getInt(2));
                b.getBookingTreatmentList().add();
                b.setBookingDate(rs.getDate(4));
                b.setBookingComment(rs.getString(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mySQLConnector.closeConnection();
        }
        return null;
    }
}

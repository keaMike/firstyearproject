package com.firstyearproject.salontina.Repositories;

import com.firstyearproject.salontina.Models.Booking;
import com.firstyearproject.salontina.Models.Treatment;
import com.firstyearproject.salontina.Services.DatabaseLogger;
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

    @Autowired
    DatabaseLogger databaseLogger;

    //Mike
    public List findBookingsByUserId(int userid) {
        try {
            String statement =  "SELECT * FROM bookings JOIN bookings_treatment ON " +
                                "bookings.bookings_id = bookings_treatment.bookings_id JOIN treatments ON " +
                                "bookings_treatment.treatments_id = treatments.treatments_id WHERE users_id = ?";

            con = mySQLConnector.openConnection();
            pstm = con.prepareStatement(statement);
            pstm.setInt(1, userid);
            ResultSet rs = pstm.executeQuery();
            List<Booking> bookings = new ArrayList<>();
            while(rs.next()) {
                Booking b = new Booking();
                b.setBookingId(rs.getInt("bookings_id"));
                b.setBookingUserId(rs.getInt("users_id"));
                //setTime?
                b.setBookingDate(rs.getDate("bookings_date"));
                b.setBookingComment(rs.getString("bookings_comment"));

                Treatment t = new Treatment();
                t.setProductId(rs.getInt("treatments_id"));
                t.setProductName(rs.getString("treatments_name"));
                t.setProductPrice(rs.getInt("treatments_price"));
                t.setProductDescription(rs.getString("treatments_description"));
                t.setTreatmentDuration(rs.getInt("treatments_duration"));
                t.setProductActive(rs.getBoolean("treatments_active"));

                //Initializing list
                List<Treatment> treatments = new ArrayList<>();
                b.setBookingTreatmentList(treatments);
                //Add treatments to a bookings treatment list
                b.getBookingTreatmentList().add(t);
                //Add bookings to ArrayList
                bookings.add(b);
            }

            databaseLogger.writeToLogFile(statement);

            return bookings;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mySQLConnector.closeConnection();
        }
        return null;
    }

    //Mike
    public boolean deleteBooking(int bookingId) {
        try {
            String statement = "DELETE FROM bookings WHERE bookings_id = ?";

            con = mySQLConnector.openConnection();
            pstm = con.prepareStatement(statement);
            pstm.setInt(1, bookingId);
            pstm.executeUpdate();

            databaseLogger.writeToLogFile(statement);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
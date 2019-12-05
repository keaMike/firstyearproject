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
import java.sql.Date;
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

    //Luca
    public boolean addBooking(Booking booking){
        String statement =  "INSERT INTO bookings " +
                            "(bookings_date, bookings_time, users_id, bookings_comment) " +
                            "VALUES " +
                            "(?, ?, ?, ?);";

        try {
            PreparedStatement pstmt = mySQLConnector.openConnection().prepareStatement(statement);

            pstmt.setString(1, booking.getBookingDate().toString());
            pstmt.setString(2, booking.getBookingTime());
            pstmt.setInt(3, booking.getBookingUserId());
            pstmt.setString(4, booking.getBookingComment());
            pstmt.execute();

            addTreatmentsToBooking(booking.getBookingTreatmentList(), booking);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    //Luca
    public boolean addTreatmentsToBooking(List<Treatment> treatmentList, Booking booking){
        String statement =  "INSERT INTO bookings_treatment " +
                            "(bookings_id, treatments_id) " +
                            "VALUES " +
                            "(?, ?)";

        for(Treatment t : treatmentList){
            try {
                PreparedStatement pstmt = mySQLConnector.openConnection().prepareStatement(statement);

                pstmt.setInt(1, getBookingId(booking));
                pstmt.setInt(2, t.getProductId());

                pstmt.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    //Luca
    private int getBookingId(Booking booking){
        String statement = "SELECT bookings_id FROM bookings WHERE bookings_date = ? AND bookings_time = ?";

        try {
            PreparedStatement pstmt = mySQLConnector.openConnection().prepareStatement(statement);

            pstmt.setDate(1, booking.getBookingDate());
            pstmt.setString(2, booking.getBookingTime());

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //Mike
    public List<Booking> findBookingsByUserId(int userid) {
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
                //TODO setTime?
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

    //Luca
    public List<Booking> getBookingList(Date startDate, Date endDate){
        String statement =  "SELECT * FROM bookings " +
                            "WHERE bookings_date BETWEEN DATE(?) AND DATE(?) " +
                            "ORDER BY bookings_date, bookings_time;";

        List<Booking> bookingList = new ArrayList<>();

        try {
            PreparedStatement pstmt = mySQLConnector.openConnection().prepareStatement(statement);

            pstmt.setDate(1, startDate);
            pstmt.setDate(2, endDate);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt(1));
                booking.setBookingUserId(rs.getInt(2));
                booking.setBookingTime(rs.getString(3));
                booking.setBookingDate(rs.getDate(4));
                booking.setBookingComment(rs.getString(5));
                bookingList.add(booking);
            }

            //TODO tilføj databaseLogger

            return bookingList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Luca
    public List<Booking> getBookingList(Date date){
        String statement =  "SELECT * FROM bookings " +
                            "WHERE bookings_date = ? " +
                            "ORDER BY bookings_time;";

        List<Booking> bookingList = new ArrayList<>();

        try {
            PreparedStatement pstmt = mySQLConnector.openConnection().prepareStatement(statement);

            pstmt.setDate(1, date);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt(1));
                booking.setBookingUserId(rs.getInt(2));
                booking.setBookingTime(rs.getString(3));
                booking.setBookingDate(rs.getDate(4));
                booking.setBookingComment(rs.getString(5));
                bookingList.add(booking);
            }

            databaseLogger.writeToLogFile(statement);

            return bookingList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Luca
    public boolean addVacationDate(Date date, int userId){
        log.info("addVacation method started...");

        for(int i = 0; i <= 7; i++){
            String time = (i + 8) + ":00";
            if(time.length() != 5){
                time = "0" + time;
            }
            Booking booking = new Booking();

            booking.setBookingDate(date);
            booking.setBookingTime(time);
            booking.setBookingUserId(userId);

            if(!addBooking(booking)){
                return false;
            }
        }
        return true;
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

            deleteTreatmentByBookingId(bookingId);

            databaseLogger.writeToLogFile(statement);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteTreatmentByBookingId(int bookingId){
        String statement = "DELETE FROM bookings_treatment WHERE bookings_id = ?";

        try {
            PreparedStatement pstmt = mySQLConnector.openConnection().prepareStatement(statement);

            pstmt.setInt(1, bookingId);

            databaseLogger.writeToLogFile(statement);

            return pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

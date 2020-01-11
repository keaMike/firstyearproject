package com.firstyearproject.salontina.Repositories;

import com.firstyearproject.salontina.Models.Booking;
import com.firstyearproject.salontina.Models.Treatment;
import com.firstyearproject.salontina.Tools.DatabaseLogger;
import com.firstyearproject.salontina.Tools.MySQLConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookingRepoImpl implements BookingRepo{

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MySQLConnector mySQLConnector;

    @Autowired
    DatabaseLogger databaseLogger;

    /**
     * Luca
     * @param booking The new booking object.
     * @return Return if the query was successful.
     */
    /*@Override
    public boolean addBooking(Booking booking){
        log.info("addBooking method started...");

        String statement =
                "INSERT INTO bookings " +
                "(bookings_date, bookings_time, users_id, bookings_comment) " +
                "VALUES " +
                "(?, ?, ?, ?);";

        try {
            PreparedStatement pstmt = mySQLConnector.openConnection().prepareStatement(statement);

            pstmt.setString(1, booking.getBookingDate().toString());
            pstmt.setString(2, booking.getBookingTime());
            pstmt.setInt(3, booking.getBookingUserId());
            pstmt.setString(4, booking.getBookingComment());

            //Returns int representing how many rows where affected by statement. If zero we know
            //that statement, has failed.
            if(pstmt.executeUpdate() <= 0){
                return false;
            }

            if(booking.getBookingTreatmentList() != null && booking.getBookingTreatmentList().size() != 0){
                addTreatmentsToBooking(booking.getBookingTreatmentList(), booking);
            }

            databaseLogger.writeToLogFile(statement);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mySQLConnector.closeConnection();
        }

        return false;
    } */

    //TODO I think this is a better method for adding bookings... Will need to test for stability. - Luca
    public boolean addBooking(Booking booking){
        log.info("addBooking method started...");

        String statement =      "INSERT INTO bookings " +
                                "(users_id, bookings_time, bookings_date, bookings_comment) " +
                                "VALUES " +
                                "(?, ?, ?, ?); ";

        String statement1 =     "INSERT bookings_treatment " +
                                "(bookings_id, treatments_id) " +
                                "VALUES " +
                                "(" +
                                    "(SELECT bookings_id FROM bookings " +
                                    "WHERE bookings_date = ? " +
                                    "AND bookings_time = ? " +
                                    "AND users_id = ?)" +
                                ", ?);";


        try {
            PreparedStatement pstmt = mySQLConnector.openConnection().prepareStatement(statement);
            PreparedStatement pstmt1 = mySQLConnector.openConnection().prepareStatement(statement1);

            pstmt.setInt(1, booking.getBookingUserId());
            pstmt.setString(2, booking.getBookingTime());
            pstmt.setDate(3, booking.getBookingDate());
            pstmt.setString(4, booking.getBookingComment());

            pstmt1.setDate(1, booking.getBookingDate());
            pstmt1.setString(2, booking.getBookingTime());
            pstmt1.setInt(3, booking.getBookingUserId());
            pstmt1.setInt(4, booking.getBookingTreatmentList().get(0).getProductId());

            pstmt.executeUpdate();
            pstmt1.executeUpdate();

            databaseLogger.writeToLogFile(statement);
            databaseLogger.writeToLogFile(statement1);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mySQLConnector.closeConnection();
        }
        return false;
    }

    /**
     * Luca
     */
    @Override
    public boolean addTreatmentsToBooking(List<Treatment> treatmentList, Booking booking){
        log.info("addTreatmentsToBooking method started...");
        String statement =
                "INSERT INTO bookings_treatment " +
                "(bookings_id, treatments_id) " +
                "VALUES (" + getBookingId(booking) + ", " + treatmentList.get(0).getProductId() + ");";

        try {
            Statement stmt = mySQLConnector.openConnection().createStatement();

            stmt.execute(statement);

            databaseLogger.writeToLogFile(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mySQLConnector.closeConnection();
        }
        return true;
    }

    /**
     * Luca
     * @return The booking id found by bookingDate and bookingTime
     */
    private int getBookingId(Booking booking){
        log.info("getBookingId method started...");
        String statement =
                "SELECT bookings_id " +
                "FROM bookings " +
                "WHERE bookings_date = ? " +
                "AND bookings_time = ?";

        try {
            PreparedStatement pstmt = mySQLConnector.openConnection().prepareStatement(statement);

            pstmt.setDate(1, booking.getBookingDate());
            pstmt.setString(2, booking.getBookingTime());

            ResultSet rs = pstmt.executeQuery();

            databaseLogger.writeToLogFile(statement);

            if(rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mySQLConnector.closeConnection();
        }
        return 0;
    }

    /**
     * Mike
     * @return List of Bookings found by userId
     */
    @Override
    public List<Booking> findBookingsByUserId(int userid) {
        log.info("findBookingsByUserId method started...");
        String statement =
                "SELECT * " +
                "FROM bookings " +
                "JOIN bookings_treatment " +
                "ON bookings.bookings_id = bookings_treatment.bookings_id " +
                "JOIN treatments " +
                "ON bookings_treatment.treatments_id = treatments.treatments_id " +
                "WHERE users_id = ? ORDER BY bookings_date, bookings_time";
        try {
            PreparedStatement pstmt = mySQLConnector.openConnection().prepareStatement(statement);
            pstmt.setInt(1, userid);
            ResultSet rs = pstmt.executeQuery();
            List<Booking> bookings = new ArrayList<>();
            while(rs.next()) {
                bookings.add(generateBookingFromResultSet(rs));
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

    /**
     * Luca
     * @return List of Bookings found by a start and end date.
     */
    @Override
    public List<Booking> getBookingList(Date startDate, Date endDate){
        log.info("getBookingList(Date, Date) method started...");
        String statement =
                "SELECT * FROM bookings " +
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

            databaseLogger.writeToLogFile(statement);

            return bookingList;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mySQLConnector.closeConnection();
        }
        return null;
    }

    /**
     * Luca
     * @return List of Bookings found by single date.
     */
    @Override
    public List<Booking> getBookingList(Date date){
        log.info("getBookingList(Date) method started...");
        String statement =
                "SELECT * FROM bookings " +
                "JOIN bookings_treatment on bookings.bookings_id = bookings_treatment.bookings_id " +
                "JOIN treatments on bookings_treatment.treatments_id = treatments.treatments_id " +
                "WHERE bookings_date = ? " +
                "ORDER BY bookings_date;";

        List<Booking> bookingList = new ArrayList<>();

        try {
            PreparedStatement pstmt = mySQLConnector.openConnection().prepareStatement(statement);

            pstmt.setDate(1, date);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                bookingList.add(generateBookingFromResultSet(rs));
            }

            databaseLogger.writeToLogFile(statement);

            return bookingList;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mySQLConnector.closeConnection();
        }
        return null;
    }

    /**
     * Luca
     * @return List of Bookings from current date and a year ahead.
     */
    @Override
    public List<Booking> getFutureBookings(){
        log.info("getFutureBookings method started...");
        String statement = "SELECT * FROM bookings " +
                            "JOIN bookings_treatment on bookings.bookings_id = bookings_treatment.bookings_id " +
                            "JOIN treatments on bookings_treatment.treatments_id = treatments.treatments_id " +
                            "WHERE bookings_date BETWEEN DATE_ADD(CURDATE(), INTERVAL 0 DAY) AND DATE_ADD(CURDATE(), INTERVAL 365 DAY) " +
                            "ORDER BY bookings_date, bookings_time;";

        List<Booking> bookingList = new ArrayList<>();
        try{
            PreparedStatement pstmt = mySQLConnector.openConnection().prepareStatement(statement);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                bookingList.add(generateBookingFromResultSet(rs));
            }

            databaseLogger.writeToLogFile(statement);

            return bookingList;
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            mySQLConnector.closeConnection();
        }

        return null;
    }

    /**
     * Luca
     * @return Booking object generates from ResultSet.
     */
    private Booking generateBookingFromResultSet(ResultSet rs) throws SQLException{
        Booking booking = new Booking();
        booking.setBookingId(rs.getInt(1));
        booking.setBookingUserId(rs.getInt(2));
        booking.setBookingTime(rs.getString(3));
        booking.setBookingDate(rs.getDate(4));
        booking.setBookingComment(rs.getString(5));

        Treatment t = new Treatment();
        t.setProductId(rs.getInt("treatments_id"));
        t.setProductName(rs.getString("treatments_name"));
        t.setProductPrice(rs.getInt("treatments_price"));
        t.setProductDescription(rs.getString("treatments_description"));
        t.setTreatmentDuration(rs.getInt("treatments_duration"));
        t.setProductActive(rs.getBoolean("treatments_active"));

        //Initializing list
        List<Treatment> treatments = new ArrayList<>();
        booking.setBookingTreatmentList(treatments);
        //Add treatments to a bookings treatment list
        booking.getBookingTreatmentList().add(t);

        return booking;
    }

    /**
     * Luca
     */
    @Override
    public boolean addVacationDate(Date date, int userId){
        log.info("addVacation method started...");

        Treatment t = new Treatment();
        t.setProductId(1);

        List<Treatment> treatmentList = new ArrayList<>();
        treatmentList.add(t);

        for(int i = 0; i <= 7; i++){
            String time = (i + 8) + ":00";
            if(time.length() != 5){
                time = "0" + time;
            }
            Booking booking = new Booking();

            booking.setBookingDate(date);
            booking.setBookingTime(time);
            booking.setBookingUserId(userId);
            booking.setBookingTreatmentList(treatmentList);

            if(!addBooking(booking)){
                return false;
            }
        }
        return true;
    }

    /**
     * Mike
     */
    @Override
    public boolean deleteBooking(int bookingId) {
        log.info("deleteBooking method started...");
        String statement = "DELETE FROM bookings WHERE bookings_id = ?";

        try {
            PreparedStatement pstmt = mySQLConnector.openConnection().prepareStatement(statement);
            pstmt.setInt(1, bookingId);
            pstmt.executeUpdate();

            databaseLogger.writeToLogFile(statement);

            return deleteTreatmentByBookingId(bookingId);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mySQLConnector.closeConnection();
        }
        return false;
    }

    /**
     * Luca
     */
    @Override
    public boolean deleteTreatmentByBookingId(int bookingId){
        log.info("deleteTreatmentByBookingId method started...");
        String statement = "DELETE FROM bookings_treatment WHERE bookings_id = ?";

        try {
            PreparedStatement pstmt = mySQLConnector.openConnection().prepareStatement(statement);

            pstmt.setInt(1, bookingId);

            databaseLogger.writeToLogFile(statement);

            return pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mySQLConnector.closeConnection();
        }
        return false;
    }

    /**
     * Asbjørn
     */
    @Override
    public boolean checkSMSReminder() {
        log.info("checkSMSReminder method started...");
        String statement =
                "SELECT * FROM smsreminders " +
                "JOIN bookings " +
                "ON smsreminders.bookings_id = bookings.bookings_id " +
                "WHERE bookings_date " +
                "BETWEEN DATE_ADD(CURDATE(), INTERVAL 1 day) AND DATE_ADD(CURDATE(), INTERVAL 1 day)";
        try {
            PreparedStatement pstmt = mySQLConnector.openConnection().prepareStatement(statement);

            ResultSet rs = pstmt.executeQuery();

            //If the resultset comes back with something in it, then the reminder have already been sent out
            if (rs.next()) {
                return true;
            }

            databaseLogger.writeToLogFile(statement);

        } catch (SQLException e) {
            e.printStackTrace();
            return true; //Incase of SQLException the thread should NOT proceed with the method
            //TODO Luca
        } finally {
            mySQLConnector.closeConnection();
        }
        return false;
    }

    /**
     * Asbjørn
     */
    @Override
    public boolean saveReminder() {
        log.info("saveReminder method started...");
        String statement =
                "INSERT INTO smsreminders " +
                "SELECT bookings_id " +
                "FROM bookings " +
                "WHERE bookings_date " +
                "BETWEEN DATE_ADD(CURDATE(), INTERVAL 1 day) AND DATE_ADD(CURDATE(), INTERVAL 1 day)";

        try{
            PreparedStatement pstmt = mySQLConnector.openConnection().prepareStatement(statement);

            pstmt.executeUpdate();

            databaseLogger.writeToLogFile(statement);

        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            mySQLConnector.closeConnection();
        }
        return true;
    }

    /**
     * Luca
     * @return Boolean stating if the booking already exists.
     */
    @Override
    public boolean bookingExists(Booking booking) {
        log.info("bookingExists method started...");
        String statement =  "SELECT * FROM bookings " +
                            "WHERE bookings_date = ? AND bookings_time = ?";

        try{
            PreparedStatement pstmt = mySQLConnector.openConnection().prepareStatement(statement);

            pstmt.setDate(1, booking.getBookingDate());
            pstmt.setString(2, booking.getBookingTime());

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                int bookingInt = rs.getInt(1);

                if(bookingInt != 0){
                    return true;
                }
            }

            databaseLogger.writeToLogFile(statement);
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            mySQLConnector.closeConnection();
        }
        return false;
    }
}

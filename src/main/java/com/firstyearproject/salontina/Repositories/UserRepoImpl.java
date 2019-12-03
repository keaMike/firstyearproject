package com.firstyearproject.salontina.Repositories;

import com.firstyearproject.salontina.Models.Reminder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.firstyearproject.salontina.Models.User;

import java.util.Random;

@Repository
public class UserRepoImpl implements UserRepo{


    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private PreparedStatement pstm;

    @Autowired
    MySQLConnector mySQLConnector;

    //Luca
    public List<String> getNewsletterList(){
        log.info("getNewsletterList method started...");

        List<String> phonenumbers = new ArrayList<>();

        String statement = "SELECT (SELECT users_phonenumber FROM users WHERE users_id = newsletter.users_id) AS phonenumber FROM newsletter;";

        try {
            PreparedStatement pstmt = mySQLConnector.openConnection().prepareStatement(statement);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                phonenumbers.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return phonenumbers;
    }

    //Luca
    public List<Reminder> getReminderList(){
        log.info("getReminderList method started...");

        List<Reminder> reminderList = new ArrayList<>();

        String statement =  "SELECT (SELECT users.users_phonenumber FROM users WHERE users.users_id = bookings.users_id) " +
                            "AS booking_phonenumber, (SELECT users.users_fullName FROM users WHERE users.users_id = bookings.users_id) " +
                            "AS booking_name, " +
                            "bookings_date, " +
                            "bookings_time " +
                            "FROM bookings WHERE bookings_date BETWEEN DATE_ADD(CURDATE(), INTERVAL 1 day) AND DATE_ADD(CURDATE(), INTERVAL 1 day);";

        try {
            PreparedStatement pstmt = mySQLConnector.openConnection().prepareStatement(statement);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                Reminder r = new Reminder();
                r.setReminderPhonenumber(rs.getString(1));
                r.setReminderUsername(rs.getString(2));
                r.setReminderDate(rs.getDate(3));
                r.setReminderTime(rs.getString(4));
                reminderList.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reminderList;
    }

    //Jonathan
    public boolean addUser(User user){
        Boolean userCreated = false;
        try{
            Connection connection = mySQLConnector.openConnection();
            PreparedStatement pstms = connection.prepareStatement("INSERT INTO users (users_fullName, users_phonenumber, users_email, users_preferences, users_password) VALUES(?, ?, ?, ?, ?)");
            pstms.setString(1, user.getUsername());
            pstms.setInt(2, user.getUserPhonenumber());
            pstms.setString(3, user.getUserEmail());
            pstms.setString(4, user.getUserPreference());
            pstms.setString(5, user.getUserPassword());
            pstms.executeUpdate();
            userCreated = true;
        } catch (Exception E) {
            E.printStackTrace();
        }
        return userCreated;
    }

    //Mike
    public List findAllUsers() {
        List users = new ArrayList();
        try {
            Connection connection = mySQLConnector.openConnection();
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM users");
            ResultSet rs = pstm.executeQuery();
            while(rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("users_id"));
                u.setUsername(rs.getString("users_fullName"));
                u.setUserPassword(rs.getString("users_password"));
                u.setUserPhonenumber(rs.getInt("users_phonenumber"));
                u.setUserEmail(rs.getString("users_email"));
                u.setUserPreference(rs.getString("users_preferences"));
                users.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    //Mike
    public User findUserById(int userid) {
        User u = new User();
        try {
            Connection con = mySQLConnector.openConnection();
            pstm = null;
            pstm = con.prepareStatement("SELECT * FROM users WHERE users_id = ?");
            pstm.setInt(1, userid);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                u.setUserId(rs.getInt(1));
                u.setUsername(rs.getString(2));
                u.setUserPassword(rs.getString(3));
                u.setUserPhonenumber(rs.getInt(4));
                u.setUserEmail(rs.getString(5));
                u.setUserPreference(rs.getString(6));
            }
            pstm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        log.info(u.toString());
        return u;
    }

    //Jonathan
    public boolean editUser(User user) {
        Boolean userEdited = false;
        log.info(user.toString());
        try{
            Connection connection = mySQLConnector.openConnection();
            PreparedStatement pstms = connection.prepareStatement("UPDATE users SET users_fullName = ?, users_phonenumber = ?, users_email = ?, users_preferences = ?, users_password = ? WHERE users_id = ?;");
            pstms.setString(1, user.getUsername());
            pstms.setInt(2,user.getUserPhonenumber());
            pstms.setString(3,user.getUserEmail());
            pstms.setString(4,user.getUserPreference());
            pstms.setString(5, user.getUserPassword());
            pstms.setInt(6,user.getUserId());
            pstms.executeUpdate();
            userEdited = true;
        } catch (Exception E){
            E.printStackTrace();
        }
        return userEdited;
    }

    public boolean editUserHistory(User user) {
        Boolean userEdited = false;
        log.info(user.toString());
        try{
            Connection connection = mySQLConnector.openConnection();
            PreparedStatement pstm = connection.prepareStatement("UPDATE users SET users_preferences = ? WHERE users_id = ?");
            pstm.setString(1,user.getUserPreference());
            pstm.setInt(2,user.getUserId());
            pstm.executeUpdate();
            userEdited = true;
        } catch (Exception E){
            E.printStackTrace();
        }
        return userEdited;
    }
}

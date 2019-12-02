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
import org.springframework.stereotype.Repository;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class UserRepoImpl implements UserRepo{


    private final Logger log = LoggerFactory.getLogger(this.getClass());

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

    public boolean addUser(User user){
        System.out.println(user.getUsername());
        Boolean userCreated = false;
        try{
            Connection connection = mySQLConnector.openConnection();
            PreparedStatement pstms = connection.prepareStatement("INSERT INTO users (users_fullName, users_phonenumber, users_email, users_preferences) VALUES(?, ?, ?, ?)");
            pstms.setString(1, user.getUsername());
            pstms.setInt(2, user.getUserPhonenumber());
            pstms.setString(3, user.getUserEmail());
            pstms.setString(4, user.getUserPreference());
            pstms.executeUpdate();
            userCreated = true;
        } catch (Exception E) {
            E.printStackTrace();
        }
        return userCreated;
    }


}

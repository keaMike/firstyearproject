package com.firstyearproject.salontina.Repositories;

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

    //Mike
    public User findDummyUser() {

        Random rand = new Random();
        int randInt = rand.nextInt(17) + 1;
        User u = new User();
        try {
            Connection con = mySQLConnector.openConnection();
            pstm = null;
            pstm = con.prepareStatement("SELECT * FROM users WHERE users_id = ?");
            pstm.setInt(1, randInt);
            ResultSet rs = pstm.executeQuery();
            while(rs.next()) {
                u.setUserId(rs.getInt(1));
                u.setUsername(rs.getString(2));
                u.setUserPhonenumber(rs.getInt(3));
                u.setUserEmail(rs.getString(4));
                u.setUserPreference(rs.getString(5));
            }
            pstm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return u;
    }


}

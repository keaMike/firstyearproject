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


@Repository
public class UserRepo {


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

    //Jonathan
    public boolean editUser(User user) {
        Boolean userEdited = false;
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

}

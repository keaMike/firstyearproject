package com.firstyearproject.salontina.Repositories;

import com.firstyearproject.salontina.Models.User;
import org.springframework.stereotype.Repository;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


@Repository
public class UserRepo {

    public boolean addUser(User user){
        System.out.println(user.getUsername());
        Boolean userCreated = false;
        try{
            //Her skal indsættes URL, USER og Password.
            Connection connection = DriverManager.getConnection("URL","User","Password");
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

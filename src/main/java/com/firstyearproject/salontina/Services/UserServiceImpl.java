package com.firstyearproject.salontina.Services;

import com.firstyearproject.salontina.Models.LoginToken;
import com.firstyearproject.salontina.Models.User;
import com.firstyearproject.salontina.Repositories.UserRepoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepoImpl UR;

    @Value("${hashing.salt}")
    private String hashingSalt;

    public boolean addUser(User user){
        user.setUserPassword(hashPassword(user.getUserPassword()));
        boolean userCreated = UR.addUser(user);
        return userCreated;
    }

    public boolean deleteUser(int userId){
        return false;
    }

    public boolean editUser(User user){
        user.setUserPassword(hashPassword(user.getUserPassword()));
        boolean userEdited = UR.editUser(user);
        return userEdited;
    }

    public User authenticateUser(LoginToken loginToken) {
        loginToken.setLoginTokenPassword(hashPassword(loginToken.getLoginTokenPassword()));
        return UR.authenticateUser(loginToken);
    }

    public boolean editUserHistory(User user){
        boolean userEdited = UR.editUserHistory(user);
        return userEdited;
    }

    public List<User> getAllUsers() {
        return UR.findAllUsers();
    }

    public User getUserById(int userid) {
        return UR.findUserById(userid);
    }

    public String hashPassword(String password){
        if(password == null){
            return null;
        }

        //Insert your own salt below
        password = password + hashingSalt;

        try {
            //Message digests are one-way-hash functions, that take data and output a hash value.

            //Here we choose to use MD5.
            MessageDigest digest = MessageDigest.getInstance("MD5");

            //We put our string into the message digest
            //and tell it to start at the start of the string and end at the end.
            digest.update(password.getBytes(), 0, password.length());

            //Here we use the BigInteger class, with the message digest
            //to output a string in base 16 (hex).
            //Hex: uses symbols 0-9 and a-f.
            String md5 = new BigInteger(1, digest.digest()).toString(16);

            return md5;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}

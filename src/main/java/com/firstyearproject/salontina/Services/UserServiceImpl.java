package com.firstyearproject.salontina.Services;

import com.firstyearproject.salontina.Models.User;
import com.firstyearproject.salontina.Repositories.UserRepoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepoImpl UR;

    //Jonathan
    public boolean addUser(User user){
        boolean userCreated = UR.addUser(user);
        return userCreated;
    }

    public boolean deleteUser(int userId){
        return false;
    }

    //Jonathan
    public boolean editUser(User user){
        boolean userEdited = UR.editUser(user);
        return userEdited;
    }

    //Mike
    public boolean editUserHistory(User user){
        boolean userEdited = UR.editUserHistory(user);
        return userEdited;
    }

    //Mike
    public List<User> getAllUsers() {
        return UR.findAllUsers();
    }

    //Mike
    public User getUserById(int userid) {
        return UR.findUserById(userid);
    }

}

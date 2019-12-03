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

    public boolean addUser(User user){
        return UR.addUser(user);

    }

    //Mike & Asbjørn
    public boolean deleteUser(int userId){
        return UR.deleteUser(userId);
    }

    public boolean editUser(User user){
        return UR.editUser(user);
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

}

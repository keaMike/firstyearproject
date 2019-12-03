package com.firstyearproject.salontina.Handlers;

import com.firstyearproject.salontina.Models.User;
import com.firstyearproject.salontina.Repositories.UserRepo;
import org.springframework.stereotype.Service;

@Service
public class UserHandler {

    private UserRepo UR = new UserRepo();

    public boolean addUser(User user){
        boolean userCreated = UR.addUser(user);
        return userCreated;
    }

    public boolean deleteUser(int userId){
        return false;
    }

    public boolean editUser(User user){
        boolean userEdited = UR.editUser(user);
        return userEdited;
    }

}

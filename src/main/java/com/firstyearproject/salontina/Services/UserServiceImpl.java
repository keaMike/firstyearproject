package com.firstyearproject.salontina.Services;

import com.firstyearproject.salontina.Models.User;
import com.firstyearproject.salontina.Repositories.UserRepoImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepoImpl UR = new UserRepoImpl();

    public boolean addUser(User user){
        boolean userCreated = UR.addUser(user);
        return userCreated;
    }

    public boolean deleteUser(int userId){
        return false;
    }

    public boolean editUser(User user){
        return false;
    }

}

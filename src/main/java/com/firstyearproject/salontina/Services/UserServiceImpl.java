package com.firstyearproject.salontina.Services;

import com.firstyearproject.salontina.Models.User;
import com.firstyearproject.salontina.Repositories.UserRepoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepoImpl UR;

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

    public User getDummyUser() {
        return UR.findDummyUser();
    }

}

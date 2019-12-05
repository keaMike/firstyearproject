package com.firstyearproject.salontina.Services;

import com.firstyearproject.salontina.Models.LoginToken;
import com.firstyearproject.salontina.Models.User;

import java.util.List;

public interface UserService {

    boolean addUser(User user);
    boolean deleteUser(int userId);
    boolean editUser(User user);
    User authenticateUser(LoginToken loginToken);
    boolean subscribeNewsletter(int userId);
    boolean unsubscribeNewsletter(int userId);
    boolean editUserHistory(User user);
    List<User> getAllUsers();
    User getUserById(int userid);

}

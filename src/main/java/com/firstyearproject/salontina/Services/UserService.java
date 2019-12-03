package com.firstyearproject.salontina.Services;

import com.firstyearproject.salontina.Models.LoginToken;
import com.firstyearproject.salontina.Models.User;

public interface UserService {

    boolean addUser(User user);
    boolean deleteUser(int userId);
    boolean editUser(User user);
    User authenticateUser(LoginToken loginToken);
}

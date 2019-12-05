package com.firstyearproject.salontina.Repositories;

import com.firstyearproject.salontina.Models.*;

import java.util.List;

public interface UserRepo {

    List<Reminder> getReminderList();
    List<String> getNewsletterList();
    boolean addUser(User user);
    boolean subscribeNewsletter(int userId);
    boolean unsubscribeNewsletter(int userId);
    List<User> findAllUsers();
    User findUserById(int userid);
    boolean editUser(User user);
    boolean editUserHistory(User user);
    User authenticateUser(LoginToken loginToken);
    boolean deleteUser(int userId);

}
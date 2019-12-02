package com.firstyearproject.salontina.Repositories;

import com.firstyearproject.salontina.Models.Reminder;
import com.firstyearproject.salontina.Models.User;

import java.util.List;

public interface UserRepo {

    List<Reminder> getReminderList();
    List<String> getNewsletterList();
    boolean addUser(User user);

}
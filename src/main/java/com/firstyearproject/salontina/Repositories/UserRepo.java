package com.firstyearproject.salontina.Repositories;

import com.firstyearproject.salontina.Models.Reminder;

import java.util.List;

public interface UserRepo {
    List<Reminder> getReminderList();
}
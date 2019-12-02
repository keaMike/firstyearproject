package com.firstyearproject.salontina.Repositories;

import com.firstyearproject.salontina.Models.User;

import java.util.List;

public interface UserRepo {
    List<String> getNewsletterList();
    boolean addUser(User user);
}
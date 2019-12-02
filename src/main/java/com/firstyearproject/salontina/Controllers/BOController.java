package com.firstyearproject.salontina.Controllers;

import com.firstyearproject.salontina.Handlers.UserHandler;
import com.firstyearproject.salontina.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller

public class BOController {

    @Autowired
    UserHandler userHandler;

    @GetMapping("/register")
    public String createUser(Model model) {
        model.addAttribute("userToBeRegistered", new User());
        return "register";
    }

    @PostMapping("/register")
    public String createUser(@ModelAttribute User user) {
        userHandler.addUser(user);
    return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("userToBeRegistered", new User());
        return "login";
    }


}

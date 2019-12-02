package com.firstyearproject.salontina.Controllers;

import com.firstyearproject.salontina.Handlers.UserHandler;
import com.firstyearproject.salontina.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class FOController {

    @Autowired
    UserHandler userHandler;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("userToBeRegistered", new User());
        return "login";
    }

    //Jonathan
    @GetMapping("/register")
    public String createUser(Model model) {
        model.addAttribute("userToBeRegistered", new User());
        return "register";
    }
    //Jonathan
    @PostMapping("/register")
    public String createUser(@ModelAttribute User user) {
        userHandler.addUser(user);
        return "redirect:/login";
    }

    @GetMapping("/redigerbruger")
    public String redigerUser(HttpSession session, Model model) {
        User user = (User)session.getAttribute("user");
        model.addAttribute("userToBeEdited", user);
        return "redigerbruger";
    }

    @PostMapping("/redigerbruger")
    public String redigerbruger(@ModelAttribute User user) {
        userHandler.addUser(user);
        return "redirect:/userprofile";
    }

    @GetMapping("userprofile")
    public String userprofile(Model model) {
        model.addAttribute("userToBeViewed", new User());
        return "userprofile";
    }
}

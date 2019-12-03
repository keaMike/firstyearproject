package com.firstyearproject.salontina.Controllers;

import com.firstyearproject.salontina.Models.LoginToken;
import com.firstyearproject.salontina.Models.User;
import com.firstyearproject.salontina.Services.UserServiceImpl;
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
    UserServiceImpl userService;

    private String LOGIN = "login";
    private String REGISTER = "register";
    private String REDIRECTREGISTER = "redirect:/" + REGISTER;

    @GetMapping("/login")
    public String login(Model model, HttpSession session) {
        model.addAttribute("loginToken", new LoginToken());
        return LOGIN;
    }

    @PostMapping("/login")
    public String login(Model model, HttpSession session, @ModelAttribute LoginToken loginToken) {
        User user = userService.authenticateUser(loginToken);

        if(user != null){
            session.setAttribute("user", user);

            return REDIRECTREGISTER;
        }
        return LOGIN;
    }

    //Jonathan
    @GetMapping("/register")
    public String createUser(Model model) {
        model.addAttribute("userToBeRegistered", new User());
        return REGISTER;
    }
    //Jonathan
    @PostMapping("/register")
    public String createUser(@ModelAttribute User user) {
        userService.addUser(user);
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
        userService.editUser(user);
        return "redirect:/userprofile";
    }

    @GetMapping("userprofile")
    public String userprofile(Model model) {
        model.addAttribute("userToBeViewed", new User());
        return "userprofile";
    }

}

package com.firstyearproject.salontina.Controllers.BackOfficeControllers;

import com.firstyearproject.salontina.Models.User;
import com.firstyearproject.salontina.Services.*;
import com.firstyearproject.salontina.Tools.ConfirmationTool;
import com.firstyearproject.salontina.Tools.UserAuthenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String ALLUSERS = "users/allusers";
    private String EDITUSERHISTORY = "users/edituserhistory";
    private String REDIRECT = "redirect:/";

    private boolean taskResult = false;


    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    BookingServiceImpl bookingServiceImpl;

    @Autowired
    UserAuthenticator userAuthenticator;

    @Autowired
    ConfirmationTool confirmationTool;


    //Mike
    @GetMapping("/userlist")
    public String userList(Model model, HttpSession session) {
        if(!userAuthenticator.userIsAdmin(session)){
            return REDIRECT;
        }
        User user = (User)session.getAttribute("user");
        List<User> users = userServiceImpl.getAllUsers();
        model.addAttribute("user", user);
        model.addAttribute("users", users);
        confirmationTool.showConfirmation(model);
        return ALLUSERS;
    }

    //Mike
    @GetMapping("/edituserhistory/{userid}")
    public String editUserHistory(@PathVariable int userid, Model model, HttpSession session) {
        if(!userAuthenticator.userIsAdmin(session)){
            return REDIRECT;
        }
        User user = (User)session.getAttribute("user");
        User editedUser = userServiceImpl.getUserById(userid);
        user.setUserHistory(bookingServiceImpl.getBookingList(editedUser.getUserId()));

        model.addAttribute("user", user);
        model.addAttribute("editedUser", editedUser);
        confirmationTool.showConfirmation(model);
        return EDITUSERHISTORY;
    }

    //Mike
    @PostMapping("/saveuserhistory")
    public String saveUserHistory(@ModelAttribute User user, HttpSession session) {
        if(!userAuthenticator.userIsAdmin(session)){
            return REDIRECT;
        }
        taskResult = userServiceImpl.editUserHistory(user);
        if (taskResult) {
            confirmationTool.confirmation("Bruger præferencerne blev gemt", ConfirmationTool.success);
            return REDIRECT + "userlist";
        } else {
            confirmationTool.confirmation("Bruger præferencerne blev ikke gemt", ConfirmationTool.danger);
            return EDITUSERHISTORY + "/" + user.getUserId();
        }
    }

}

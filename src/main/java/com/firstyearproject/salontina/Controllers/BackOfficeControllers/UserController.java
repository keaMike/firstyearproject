package com.firstyearproject.salontina.Controllers.BackOfficeControllers;

import com.firstyearproject.salontina.Models.User;
import com.firstyearproject.salontina.Services.*;
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
    private boolean showConfirmation = false;
    private String confirmationText = "";
    private String alert = "";
    private String danger = "alert alert-danger";
    private String success = "alert alert-success";


    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    BookingServiceImpl bookingServiceImpl;

    @Autowired
    UserAuthenticator userAuthenticator;

    //Luca
    //Used in Java Methods/mappings
    public void confirmation(String text, String alert){
        showConfirmation = true;
        confirmationText = text;
        this.alert = alert;
    }

    //Luca
    //Used in HTML-Modals
    public void showConfirmation(Model model){
        model.addAttribute("showconfirmation", showConfirmation);
        model.addAttribute("confirmationtext", confirmationText);
        model.addAttribute("alert", alert);
        showConfirmation = false;
    }

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
        showConfirmation(model);
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
        showConfirmation(model);
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
            confirmation("Bruger præferencerne blev gemt", success);
            return REDIRECT + "userlist";
        } else {
            confirmation("Bruger præferencerne blev ikke gemt", danger);
            return EDITUSERHISTORY + "/" + user.getUserId();
        }
    }

}

package com.firstyearproject.salontina.Controllers.BackOfficeControllers;

import com.firstyearproject.salontina.Models.User;
import com.firstyearproject.salontina.Services.BookingServiceImpl;
import com.firstyearproject.salontina.Services.ProductServiceImpl;
import com.firstyearproject.salontina.Services.SMSServiceImpl;
import com.firstyearproject.salontina.Services.UserServiceImpl;
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

    private boolean taskResult = false;
    private boolean showConfirmation = false;
    private String confirmationText = "";


    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    BookingServiceImpl bookingServiceImpl;

    //Luca
    //Used in Java Methods/mappings
    public void confirmation(String text){
        showConfirmation = true;
        confirmationText = text;
    }

    //Luca
    //Used in HTML-Modals
    public void showConfirmation(Model model){
        model.addAttribute("showconfirmation", true);
        model.addAttribute("confirmationtext", confirmationText);
        showConfirmation = false;
    }

    //Mike
    @GetMapping("/userlist")
    public String userList(Model model, HttpSession session) {
        User user = (User)session.getAttribute("user");
        List<User> users = userServiceImpl.getAllUsers();
        model.addAttribute("user", user);
        model.addAttribute("users", users);
        return ALLUSERS;
    }

    //Mike
    @GetMapping("/edituserhistory/{userid}")
    public String editUserHistory(@PathVariable int userid, Model model, HttpSession session) {
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        User editedUser = userServiceImpl.getUserById(userid);
        user.setUserHistory(bookingServiceImpl.getBookingList(user.getUserId()));
        model.addAttribute("editedUser", editedUser);
        return EDITUSERHISTORY;
    }

    //Mike
    @PostMapping("/saveuserhistory")
    public String saveUserHistory(@ModelAttribute User user) {
        taskResult = userServiceImpl.editUserHistory(user);
        if (taskResult) {
            return EDITUSERHISTORY;
        } else {
            return EDITUSERHISTORY + user.getUserId();
        }
    }

}

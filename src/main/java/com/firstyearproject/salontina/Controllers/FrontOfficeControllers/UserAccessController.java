package com.firstyearproject.salontina.Controllers.FrontOfficeControllers;

import com.firstyearproject.salontina.Models.Booking;
import com.firstyearproject.salontina.Models.LoginToken;
import com.firstyearproject.salontina.Models.User;
import com.firstyearproject.salontina.Services.BookingServiceImpl;
import com.firstyearproject.salontina.Services.ProductServiceImpl;
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
public class UserAccessController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String INDEX = "index";
    private String REDIRECT = "redirect:/";
    private String LOGIN = "login";
    private String REGISTER = "users/register";
    private String EDITUSER = "users/edituser";
    private String USERPROFILE = "users/userprofile";
    private String MYPROFILE = "users/myprofile";
    private String CONTACT = "contact";

    private boolean taskResult = false;
    private boolean showConfirmation = false;
    private String confirmationText = "";

    @Autowired
    UserServiceImpl userService;

    @Autowired
    BookingServiceImpl bookingService;

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
    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        if(session.getAttribute("user") != null) {
            User user = (User)session.getAttribute("user");
            model.addAttribute("user", user);
            log.info(user.toString());
        } else {
            model.addAttribute("user", new User());
            model.addAttribute("loginToken", new LoginToken());
        }
        return INDEX;
    }

    //Luca
    @PostMapping("/")
    public String login(Model model, HttpSession session, @ModelAttribute LoginToken loginToken) {
        User user = userService.authenticateUser(loginToken);

        if(user != null){
            session.setAttribute("user", user);

            return REDIRECT;
        }
        return INDEX;
    }

    //Jonathan
    @PostMapping("/register")
    public String register(@ModelAttribute User user) {
        userService.addUser(user);
        return REDIRECT;
    }
    //Jonathan
    @GetMapping("/editUser")
    public String editUser(HttpSession session, Model model) {
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        return EDITUSER;
    }
    //Jonathan
    @PostMapping("/editUser")
    public String editUser(@ModelAttribute User user) {
        userService.editUser(user);
        return REDIRECT + USERPROFILE;
    }

    @GetMapping("userprofile")
    public String userprofile(Model model, HttpSession session) {
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        return USERPROFILE;
    }

    //Mike
    @GetMapping("/deleteuser/{userid}")
    public String deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
        return REDIRECT + EDITUSER;
    }

    //Mike
    @GetMapping("/myprofile")
    public String myprofil(Model model, HttpSession session) {
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        return MYPROFILE;
    }

    //Mike
    @GetMapping("/contact")
    public String contact(Model model, HttpSession session) {
        if(session.getAttribute("user") != null) {
            User user = (User)session.getAttribute("user");
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", new User());
            model.addAttribute("loginToken", new LoginToken());
        }
        return CONTACT;
    }

    //Asbjørn
    @PostMapping("subscribeNewsletter")
    public String subscribeNewsletter (@ModelAttribute User user) {
        taskResult = userService.subscribeNewsletter(user.getUserId());
        if (taskResult) {
            confirmation("Du er blevet tilmeldt nyhedsbrevet");
            return EDITUSER;
        }
        confirmation("Vi kunne IKKE tilmelde dig nyhedsbrevet. Prøv igen senere");
        return REDIRECT;
    }

    //Asbjørn
    @PostMapping("unsubscribeNewsletter")
    public String unsubscribeNewsletter (@ModelAttribute User user) {
        taskResult = userService.unsubscribeNewsletter(user.getUserId());
        if (taskResult) {
            confirmation("Du er blevet afmeldt nyhedsbrevet");
            return EDITUSER;
        }
        confirmation("Vi kunne IKKE afmelde dig nyhedsbrevet. Prøv igen senere");
        return EDITUSER;
    }

    @GetMapping ("/contact1")
    public String contact () {
        return CONTACT;
    }

}

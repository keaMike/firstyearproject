package com.firstyearproject.salontina.Controllers.FrontOfficeControllers;

import com.firstyearproject.salontina.Models.Booking;
import com.firstyearproject.salontina.Models.LoginToken;
import com.firstyearproject.salontina.Models.User;
import com.firstyearproject.salontina.Services.BookingServiceImpl;
import com.firstyearproject.salontina.Services.ProductServiceImpl;
import com.firstyearproject.salontina.Services.UserAuthenticator;
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
    private String alert = "";
    private String danger = "alert alert-danger";
    private String success = "alert alert-success";

    @Autowired
    UserServiceImpl userService;

    @Autowired
    BookingServiceImpl bookingService;

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

    //Jonathan & Mike
    private Model userExcists(Model model, HttpSession session) {
        if(session.getAttribute("user") != null) {
            User user = (User)session.getAttribute("user");
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", new User());
            model.addAttribute("loginToken", new LoginToken());
        }
        return model;
    }


    //Mike
    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        userExcists(model, session);
        showConfirmation(model);
        return INDEX;
    }

    //Luca
    @PostMapping("/")
    public String login(HttpSession session, @ModelAttribute LoginToken loginToken) {
        User user = userService.authenticateUser(loginToken);

        if(user != null){
            session.setAttribute("user", user);
            confirmation("Velkommen " + user.getUsername() + ", du er blevet logget ind", success);
            return REDIRECT;
        }
        confirmation("Fejl ved login, enten din email/tlf. eller kodeord er forkert", danger);
        return REDIRECT;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        if(!userAuthenticator.userIsUser(session)){
            return REDIRECT;
        }
        session.removeAttribute("user");
        return REDIRECT;
    }

    //Jonathan
    @PostMapping("/register")
    public String register(@ModelAttribute User user) {
        taskResult = userService.addUser(user);
        if (taskResult) {
            confirmation("Du er blevet oprettet som bruger", success);
            return REDIRECT;
        }
        confirmation("Vi kunne ikke oprette dig som bruger. Prøv igen", danger);
        return REDIRECT;
    }

    //Jonathan
    @GetMapping("/editUser")
    public String editUser(HttpSession session, Model model) {
        if(!userAuthenticator.userIsUser(session)){
             return REDIRECT;
        }
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        return EDITUSER;
    }

    //Jonathan
    @PostMapping("/editUser")
    public String editUser(@ModelAttribute User user, HttpSession session) {
        if(!userAuthenticator.userIsUser(session)){
            return REDIRECT;
        }
        taskResult = userService.editUser(user);
        User editedUser = userService.getUserById(user.getUserId());
        session.setAttribute("user", editedUser);
        if (taskResult) {
            confirmation("Ændringerne er blevet gemt", success);
            return REDIRECT + "userprofile";
        }
        confirmation("Vi kunne ikke gemme dine ændringer. Prøv igen", danger);
        return REDIRECT + "userprofile";
    }

    @GetMapping("userprofile")
    public String userprofile(Model model, HttpSession session) {
        log.info("userprofile action started...");
        if(!userAuthenticator.userIsUser(session)){
            return REDIRECT;
        }
        log.info("user is admin or user...");
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        showConfirmation(model);
        return USERPROFILE;
    }

    //Mike
    @PostMapping("/deleteuser")
    public String deleteUser(@ModelAttribute User user, HttpSession session) {
        if(!userAuthenticator.userIsUser(session)){
            return REDIRECT;
        }
        confirmation("Din profil blev successfuldt slettet", success);
        session.removeAttribute("user");
        userService.deleteUser(user.getUserId());
        return REDIRECT;
    }

    //Mike
    @GetMapping("/myprofile")
    public String myprofil(Model model, HttpSession session) {
        if(!userAuthenticator.userIsUser(session)){
            return REDIRECT;
        }
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        return MYPROFILE;
    }

    //Mike
    @GetMapping("/contact")
    public String contact(Model model, HttpSession session) {
        userExcists(model, session);
        return CONTACT;
    }

    //Asbjørn
    @PostMapping("subscribeNewsletter")
    public String subscribeNewsletter (@ModelAttribute User user, HttpSession session) {
        if(!userAuthenticator.userIsUser(session)){
            return REDIRECT;
        }
        taskResult = userService.subscribeNewsletter(user.getUserId());
        if (taskResult) {
            confirmation("Du er blevet tilmeldt nyhedsbrevet", success);
            return REDIRECT + "userprofile";
        }
        confirmation("Vi kunne ikke tilmelde dig nyhedsbrevet. Prøv igen senere", danger);
        return REDIRECT + "userprofile";
    }

    //Asbjørn
    @PostMapping("unsubscribeNewsletter")
    public String unsubscribeNewsletter (@ModelAttribute User user, HttpSession session) {
        if(!userAuthenticator.userIsUser(session)){
            return REDIRECT;
        }
        taskResult = userService.unsubscribeNewsletter(user.getUserId());
        if (taskResult) {
            confirmation("Du er blevet afmeldt nyhedsbrevet", success);
            return REDIRECT + "userprofile";
        }
        confirmation("Vi kunne ikke afmelde dig nyhedsbrevet. Prøv igen senere", danger);
        return REDIRECT + "userprofile";
    }

}

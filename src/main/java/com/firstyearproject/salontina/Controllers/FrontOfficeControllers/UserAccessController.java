package com.firstyearproject.salontina.Controllers.FrontOfficeControllers;

import com.firstyearproject.salontina.Models.LoginToken;
import com.firstyearproject.salontina.Models.User;
import com.firstyearproject.salontina.Services.BookingServiceImpl;
import com.firstyearproject.salontina.Tools.ConfirmationTool;
import com.firstyearproject.salontina.Tools.SessionLog;
import com.firstyearproject.salontina.Tools.UserAuthenticator;
import com.firstyearproject.salontina.Services.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

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

    @Autowired
    UserServiceImpl userService;

    @Autowired
    BookingServiceImpl bookingService;

    @Autowired
    UserAuthenticator userAuthenticator;

    @Autowired
    ConfirmationTool confirmationTool;

    //Jonathan & Mike
    private Model userExists(Model model, HttpSession session) {
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
        log.info("get index action started..." + SessionLog.sessionId(session));

        userExists(model, session);
        confirmationTool.showConfirmation(model);
        return INDEX;
    }

    //Luca
    @PostMapping("/")
    public String login(HttpSession session, @ModelAttribute LoginToken loginToken) {
        log.info("post login action started..." + SessionLog.sessionId(session));

        User user = userService.authenticateUser(loginToken);

        if(user != null){
            session.setAttribute("user", user);
            confirmationTool.confirmation("Velkommen " + user.getUsername() + ", du er blevet logget ind", ConfirmationTool.success);
            return REDIRECT;
        }
        confirmationTool.confirmation("Fejl ved login, enten din email/tlf. eller kodeord er forkert", ConfirmationTool.danger);
        return REDIRECT;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        log.info("get logout action started..." + SessionLog.sessionId(session));

        if(!userAuthenticator.userIsUser(session)){
            return REDIRECT;
        }
        session.removeAttribute("user");
        return REDIRECT;
    }

    //Jonathan
    @PostMapping("/register")
    public String register(HttpSession session, @ModelAttribute User user) {
        log.info("post register action started..." + SessionLog.sessionId(session));

        taskResult = userService.addUser(user);
        if (taskResult) {
            confirmationTool.confirmation("Du er blevet oprettet som bruger", ConfirmationTool.success);
            return REDIRECT;
        }
        confirmationTool.confirmation("Vi kunne ikke oprette dig som bruger. Prøv igen", ConfirmationTool.danger);
        return REDIRECT;
    }

    //Jonathan
    @GetMapping("/editUser")
    public String editUser(HttpSession session, Model model) {
        log.info("get editUser action started..." + SessionLog.sessionId(session));

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
        log.info("post editUser action started..." + SessionLog.sessionId(session));

        if(!userAuthenticator.userIsUser(session)){
            return REDIRECT;
        }
        taskResult = userService.editUser(user);
        User editedUser = userService.getUserById(user.getUserId());
        session.setAttribute("user", editedUser);
        if (taskResult) {
            confirmationTool.confirmation("Ændringerne er blevet gemt", ConfirmationTool.success);
            return REDIRECT + "userprofile";
        }
        confirmationTool.confirmation("Vi kunne ikke gemme dine ændringer. Prøv igen", ConfirmationTool.danger);
        return REDIRECT + "userprofile";
    }

    @GetMapping("userprofile")
    public String userprofile(Model model, HttpSession session) {
        log.info("get userprofile action started..." + SessionLog.sessionId(session));

        log.info("userprofile action started...");
        if(!userAuthenticator.userIsUser(session)){
            return REDIRECT;
        }
        log.info("user is admin or user...");
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        confirmationTool.showConfirmation(model);
        return USERPROFILE;
    }

    //Mike
    @PostMapping("/deleteuser")
    public String deleteUser(@ModelAttribute User user, HttpSession session) {
        log.info("post deleteuser action started..." + SessionLog.sessionId(session));

        if(!userAuthenticator.userIsUser(session)){
            return REDIRECT;
        }
        confirmationTool.confirmation("Din profil blev successfuldt slettet", ConfirmationTool.success);
        session.removeAttribute("user");
        userService.deleteUser(user.getUserId());
        return REDIRECT;
    }

    //Mike
    @GetMapping("/myprofile")
    public String myprofil(Model model, HttpSession session) {
        log.info("get myprofile action started..." + SessionLog.sessionId(session));

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
        log.info("get contact action started..." + SessionLog.sessionId(session));

        userExists(model, session);
        return CONTACT;
    }

    //Asbjørn
    @PostMapping("subscribeNewsletter")
    public String subscribeNewsletter (@ModelAttribute User user, HttpSession session) {
        log.info("post subscribeNewsletter action started..." + SessionLog.sessionId(session));

        if(!userAuthenticator.userIsUser(session)){
            return REDIRECT;
        }
        taskResult = userService.subscribeNewsletter(user.getUserId());
        if (taskResult) {
            confirmationTool.confirmation("Du er blevet tilmeldt nyhedsbrevet", ConfirmationTool.success);
            return REDIRECT + "userprofile";
        }
        confirmationTool.confirmation("Vi kunne ikke tilmelde dig nyhedsbrevet. Prøv igen senere", ConfirmationTool.danger);
        return REDIRECT + "userprofile";
    }

    //Asbjørn
    @PostMapping("unsubscribeNewsletter")
    public String unsubscribeNewsletter (@ModelAttribute User user, HttpSession session) {
        log.info("post unsubscribeNewsletter action started..." + SessionLog.sessionId(session));

        if(!userAuthenticator.userIsUser(session)){
            return REDIRECT;
        }
        taskResult = userService.unsubscribeNewsletter(user.getUserId());
        if (taskResult) {
            confirmationTool.confirmation("Du er blevet afmeldt nyhedsbrevet", ConfirmationTool.success);
            return REDIRECT + "userprofile";
        }
        confirmationTool.confirmation("Vi kunne ikke afmelde dig nyhedsbrevet. Prøv igen senere", ConfirmationTool.danger);
        return REDIRECT + "userprofile";
    }

}

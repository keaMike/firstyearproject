package com.firstyearproject.salontina.Controllers.BackOfficeControllers;

import com.firstyearproject.salontina.Models.AddVacation;
import com.firstyearproject.salontina.Models.User;
import com.firstyearproject.salontina.Repositories.BookingRepoImpl;
import com.firstyearproject.salontina.Services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Controller
public class AdminController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String ADDVACATION = "addvacation";
    private String CONTROLPANEL = "controlpanel";
    private String REDIRECT = "redirect:/";

    private boolean showConfirmation = false;
    private String confirmationText = "";

    @Autowired
    BookingServiceImpl bookingService;
    @Autowired
    SMSServiceImpl smsService;
    @Autowired
    UserAuthenticator userAuthenticator;

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
    @GetMapping("/controlpanel")
    public String controlpanel(Model model, HttpSession session) {
        if(!userAuthenticator.userIsAdmin(session)){
            return REDIRECT;
        }
        smsService.initiateAutoReminder("Initate");
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        return CONTROLPANEL;
    }

    @GetMapping("/addvacation")
    public String addVacation(HttpSession session, Model model){
        if(!userAuthenticator.userIsAdmin(session)){
            return REDIRECT;
        }
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("vacationstring", new AddVacation());
        return ADDVACATION;
    }

    @PostMapping("/addvacation")
    public String addVacation(HttpSession session, @ModelAttribute AddVacation vacationString){
        if(!userAuthenticator.userIsAdmin(session)){
            return REDIRECT;
        }
        User user = (User)session.getAttribute("user");
        bookingService.addVacationDate(vacationString.getString(), user.getUserId());
        return REDIRECT + ADDVACATION;
    }

}

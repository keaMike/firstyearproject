package com.firstyearproject.salontina.Controllers.BackOfficeControllers;

import com.firstyearproject.salontina.Models.AddVacation;
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
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class AdminController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String ADDVACATION = "addvacation";
    private String CONTROLPANEL = "controlpanel";
    private String REDIRECT = "redirect:/";

    @Autowired
    BookingServiceImpl bookingService;

    @Autowired
    SMSServiceImpl smsService;

    @Autowired
    UserAuthenticator userAuthenticator;

    @Autowired
    AutoReminderServiceImpl autoReminderService;

    @Autowired
    ConfirmationTool confirmationTool;

    //Mike
    @GetMapping("/controlpanel")
    public String controlpanel(Model model, HttpSession session) {
        if(!userAuthenticator.userIsAdmin(session)){
            return REDIRECT;
        }
        autoReminderService.initiateAutoReminder();
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
        if(bookingService.addVacationDate(vacationString.getString(), user.getUserId())){

        }
        return REDIRECT + ADDVACATION;
    }

}

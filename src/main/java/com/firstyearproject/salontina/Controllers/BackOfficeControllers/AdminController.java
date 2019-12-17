package com.firstyearproject.salontina.Controllers.BackOfficeControllers;

import com.firstyearproject.salontina.Models.ChooseDate;
import com.firstyearproject.salontina.Models.User;
import com.firstyearproject.salontina.Services.*;
import com.firstyearproject.salontina.Tools.ConfirmationTool;
import com.firstyearproject.salontina.Tools.SessionLog;
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

    /**
     * Mike
     */
    @GetMapping("/controlpanel")
    public String controlpanel(Model model, HttpSession session) {
        log.info("get controlpanel action started..." + SessionLog.sessionId(session));

        if(!userAuthenticator.userIsAdmin(session)){
            log.info(SessionLog.NOTADMIN + SessionLog.sessionId(session));

            return REDIRECT;
        }
        autoReminderService.initiateAutoReminder();
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        return CONTROLPANEL;
    }

    /**
     * Luca
     */
    @GetMapping("/addvacation")
    public String addVacation(HttpSession session, Model model){
        log.info("get addvacation action started..." + SessionLog.sessionId(session));

        if(!userAuthenticator.userIsAdmin(session)){
            log.info(SessionLog.NOTADMIN + SessionLog.sessionId(session));

            return REDIRECT;
        }

        confirmationTool.showConfirmation(model);

        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("choosedate", new ChooseDate());
        return ADDVACATION;
    }

    /**
     * Luca
     */
    @PostMapping("/addvacation")
    public String addVacation(HttpSession session, @ModelAttribute ChooseDate chooseDate){
        log.info("post addvacation action started..." + SessionLog.sessionId(session));

        if(!userAuthenticator.userIsAdmin(session)){
            log.info(SessionLog.NOTADMIN + SessionLog.sessionId(session));

            return REDIRECT;
        }
        User user = (User)session.getAttribute("user");
        if(bookingService.addVacationDate(chooseDate.getDate().toString(), user.getUserId())){
            log.info("added vacation on date: " + chooseDate + "..." + SessionLog.sessionId(session));

            confirmationTool.confirmation("Ferie er blevet tilføjet d. " + chooseDate + ".", ConfirmationTool.success);
            return REDIRECT + ADDVACATION;
        }
        log.info("could not add vacation on date: " + chooseDate + "..." + SessionLog.sessionId(session));

        confirmationTool.confirmation("Ferie kunne ikke tilføjes d. " + chooseDate + ".", ConfirmationTool.danger);
        return REDIRECT + ADDVACATION;
    }

}

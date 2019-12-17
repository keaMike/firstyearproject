package com.firstyearproject.salontina.Controllers.BackOfficeControllers;

import com.firstyearproject.salontina.Models.Newsletter;
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
public class SMSController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String NEWSLETTER = "sms/newsletter";
    private String REMINDER = "sms/reminder";
    private String NEWSLETTERORREMINDER = "sms/newsletterOrReminder";
    private String REDIRECT = "redirect:/";

    @Autowired
    SMSServiceImpl sMSServiceImpl;

    @Autowired
    AutoReminderServiceImpl autoReminderService;

    @Autowired
    UserAuthenticator userAuthenticator;

    @Autowired
    ConfirmationTool confirmationTool;

    /**
     * Luca
     */
    @GetMapping("reminder")
    public String reminder(Model model, HttpSession session) {
        log.info("get reminder action started..." + SessionLog.sessionId(session));

        if(!userAuthenticator.userIsAdmin(session)){
            log.info(SessionLog.NOTADMIN + SessionLog.sessionId(session));

            return REDIRECT;
        }

        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        confirmationTool.showConfirmation(model);

        return REMINDER;
    }

    /**
     * Luca
     */
    @GetMapping("sendreminder")
    public String sendreminder(Model model, HttpSession session) {
        log.info("get sendreminder action started..." + SessionLog.sessionId(session));

        if(!userAuthenticator.userIsAdmin(session)){
            log.info(SessionLog.NOTADMIN + SessionLog.sessionId(session));

            return REDIRECT;
        }

        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        if(sMSServiceImpl.sendReminder()){
            log.info("sms reminder sent successfully..." + SessionLog.sessionId(session));
            confirmationTool.confirmation("SMS Reminder blev sendt.", ConfirmationTool.success);
        } else{
            log.info("sms reminder failed to sent..." + SessionLog.sessionId(session));
            confirmationTool.confirmation("Der skete en fejl ved afsendelse af reminder.", ConfirmationTool.danger);
        }

        return REDIRECT;
    }

    /**
     * Luca
     */
    @GetMapping("newsletter")
    public String newsletter(Model model, HttpSession session) {
        log.info("get newsletter action started..." + SessionLog.sessionId(session));

        if(!userAuthenticator.userIsAdmin(session)){
            log.info(SessionLog.NOTADMIN + SessionLog.sessionId(session));

            return REDIRECT;
        }

        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("newsletter", new Newsletter());
        confirmationTool.showConfirmation(model);

        return NEWSLETTER;
    }

    /**
     * Luca
     */
    @PostMapping("sendnewsletter")
    public String sendNewsletter(Model model, HttpSession session, @ModelAttribute Newsletter newsletter) {
        log.info("post sendnewsletter action started..." + SessionLog.sessionId(session));

        if(!userAuthenticator.userIsAdmin(session)){
            log.info(SessionLog.NOTADMIN + SessionLog.sessionId(session));

            return REDIRECT;
        }

        if(sMSServiceImpl.sendNewsletter(newsletter.getText())){
            log.info("newsletter was successfully sent..." + SessionLog.sessionId(session));
            confirmationTool.confirmation("Nyhedsbrev blev sendt.", ConfirmationTool.success);
        } else{
            log.info("sms newsletter failed to sent..." + SessionLog.sessionId(session));
            confirmationTool.confirmation("Der skete en fejl ved afsendelse af nyhedsbrev.", ConfirmationTool.danger);
        }

        return REDIRECT + NEWSLETTER;
    }

    /**
     * Luca
     */
    @PostMapping("sendtestnewsletter")
    public String sendTestNewsletter(Model model, HttpSession session, @ModelAttribute Newsletter newsletter) {
        log.info("post sendtestnewsletter action started..." + SessionLog.sessionId(session));

        if(!userAuthenticator.userIsAdmin(session)){
            log.info(SessionLog.NOTADMIN + SessionLog.sessionId(session));

            return REDIRECT;
        }

        if(sMSServiceImpl.sendNewsletterTest(newsletter.getTestNumber(), newsletter.getText())){
            log.info("newsletter was successfully sent..." + SessionLog.sessionId(session));
            confirmationTool.confirmation("Test nyhedsbrev blev sendt.", ConfirmationTool.success);
        } else{
            log.info("sms reminder failed to sent..." + SessionLog.sessionId(session));
            confirmationTool.confirmation("Der skete en fejl ved afsendelse af nyhedsbrev.", ConfirmationTool.danger);
        }

        return REDIRECT + NEWSLETTER;
    }

    /**
     * Mike
     */
    @GetMapping("/sms")
    public String sendMessage(Model model, HttpSession session) {
        log.info("get sms action started..." + SessionLog.sessionId(session));

        if(!userAuthenticator.userIsAdmin(session)){
            log.info(SessionLog.NOTADMIN + SessionLog.sessionId(session));

            return REDIRECT;
        }
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        return NEWSLETTERORREMINDER;
    }

    /**
     * Asbjørn
     * Manually starts the autoReminder
     */
    @GetMapping ("/startAutoReminder")
    public String startAutoReminder(HttpSession session) {
        log.info("post startAutoReminder action started..." + SessionLog.sessionId(session));

        if(!userAuthenticator.userIsAdmin(session)){
            log.info(SessionLog.NOTADMIN + SessionLog.sessionId(session));

            return REDIRECT;
        }
        autoReminderService.initiateAutoReminder();
        if (true) {
            log.info("AutoReminder succesfully started..." + SessionLog.sessionId(session));
            confirmationTool.confirmation("SMS påmindelse er i gangsat", ConfirmationTool.success);
        } else {
            log.info("AutoReminder could not be started..." + SessionLog.sessionId(session));
            confirmationTool.confirmation("SMS påmindelse kunne ikke startes", ConfirmationTool.danger);
        }
        return REDIRECT;
    }

    /**
     * Asbjørn
     * Manually stops the autoReminder
     */
    @GetMapping ("/stopAutoReminder")
    public String stopAutoReminde(HttpSession session){
        log.info("post stopAutoReminder action started..." + SessionLog.sessionId(session));

        if(!userAuthenticator.userIsAdmin(session)){
            log.info(SessionLog.NOTADMIN + SessionLog.sessionId(session));

            return REDIRECT;
        }
        if(autoReminderService.cancelAutoReminder()) {
            log.info("Autoreminder succesfully stopped..." + SessionLog.sessionId(session));
            confirmationTool.confirmation("SMS påmindelse er blevet stoppet", ConfirmationTool.success);
        } else{
            log.info("AutoReminder failed to stop..." + SessionLog.sessionId(session));
            confirmationTool.confirmation("SMS påmindelse kunne ikke stoppes", ConfirmationTool.danger);
        }
        return REDIRECT;
    }
}

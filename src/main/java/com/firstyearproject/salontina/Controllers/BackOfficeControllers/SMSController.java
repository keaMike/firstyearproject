package com.firstyearproject.salontina.Controllers.BackOfficeControllers;

import com.firstyearproject.salontina.Models.Newsletter;
import com.firstyearproject.salontina.Models.User;
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
import javax.swing.text.html.HTML;

@Controller
public class SMSController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String NEWSLETTER = "sms/newsletter";
    private String REMINDER = "sms/reminder";
    private String NEWSLETTERORREMINDER = "sms/newsletterOrReminder";
    private String REDIRECT = "redirect:/";

    private boolean showConfirmation = false;
    private String confirmationText = "";
    private String alert = "";
    private String danger = "alert alert-danger";
    private String success = "alert alert-success";

    @Autowired
    SMSServiceImpl sMSServiceImpl;

    @Autowired
    AutoReminderServiceImpl autoReminderService;

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
        showConfirmation = false;
    }

    //Luca
    @GetMapping("reminder")
    public String reminder(Model model, HttpSession session) {
        if(!userAuthenticator.userIsAdmin(session)){
            return REDIRECT;
        }
        log.info("get reminder action started...");
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        showConfirmation(model);

        return REMINDER;
    }

    //Luca
    @GetMapping("sendreminder")
    public String sendreminder(Model model, HttpSession session) {
        if(!userAuthenticator.userIsAdmin(session)){
            return REDIRECT;
        }
        log.info("post sendreminder action started...");
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        if(sMSServiceImpl.sendReminder()){
            log.info("sms reminder sent successfully...");
            confirmation("SMS Reminder blev sendt.", success);
        } else{
            log.info("sms reminder failed to sent...");
            confirmation("Der skete en fejl ved afsendelse af reminder.", danger);
        }

        return REDIRECT + "reminder";
    }

    //Luca
    @GetMapping("newsletter")
    public String newsletter(Model model, HttpSession session) {
        if(!userAuthenticator.userIsAdmin(session)){
            return REDIRECT;
        }
        log.info("get newsletter action started...");
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("newsletter", new Newsletter());
        showConfirmation(model);

        return NEWSLETTER;
    }

    //Luca
    @PostMapping("sendnewsletter")
    public String sendNewsletter(Model model, HttpSession session, @ModelAttribute Newsletter newsletter) {
        if(!userAuthenticator.userIsAdmin(session)){
            return REDIRECT;
        }
        log.info("post newsletter action started...");

        if(sMSServiceImpl.sendNewsletter(newsletter.getText())){
            log.info("newsletter was successfully sent...");
            confirmation("Nyhedsbrev blev sendt.", success);
        } else{
            log.info("sms newsletter failed to sent...");
            confirmation("Der skete en fejl ved afsendelse af nyhedsbrev.", danger);
        }

        return REDIRECT + "newsletter";
    }

    //Luca
    @PostMapping("sendtestnewsletter")
    public String sendTestNewsletter(Model model, HttpSession session, @ModelAttribute Newsletter newsletter) {
        if(!userAuthenticator.userIsAdmin(session)){
            return REDIRECT;
        }
        log.info("post newsletter action started...");

        if(sMSServiceImpl.sendNewsletterTest(newsletter.getTestNumber(), newsletter.getText())){
            log.info("newsletter was successfully sent...");
            confirmation("Test nyhedsbrev blev sendt.", success);
        } else{
            log.info("sms reminder failed to sent...");
            confirmation("Der skete en fejl ved afsendelse af nyhedsbrev.", danger);
        }

        return REDIRECT + "newsletter";
    }

    //Mike
    @GetMapping("/sendbesked")
    public String sendMessage(Model model, HttpSession session) {
        if(!userAuthenticator.userIsAdmin(session)){
            return REDIRECT;
        }
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        return NEWSLETTERORREMINDER;
    }

    //Asbjørn
    //Manually starts the autoReminder
    @PostMapping ("/startAutoReminder")
    public String startAutoReminder(HttpSession session) {
        if(!userAuthenticator.userIsAdmin(session)){
            return REDIRECT;
        }
        autoReminderService.initiateAutoReminder();
        if (true) {
            log.info("AutoReminder succesfully started");
            confirmation("SMS påmindelse er i gangsat");
        } else {
            log.info("AutoReminder could not be started");
            confirmation("SMS påmindelse kunne ikke startes");
        }
        return REDIRECT + REMINDER;
    }

    //Asbjørn
    //Manually stops the autoReminder
    @PostMapping ("/stopAutoReminder")
    public String stopAutoReminde(HttpSession session){
        if(!userAuthenticator.userIsAdmin(session)){
            return REDIRECT;
        }
        if(autoReminderService.cancelAutoReminder()) {
            log.info("Autoreminder succesfully stopped");
            confirmation("SMS påmindelse er blevet stoppet");
        } else{
            log.info("AutoReminder failed to stop...");
            confirmation("SMS påmindelse kunne ikke stoppes");
        }
        return REDIRECT + REMINDER;
    }
}

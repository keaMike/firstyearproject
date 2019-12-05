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

    @Autowired
    SMSServiceImpl sMSServiceImpl;

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

    //Luca
    @GetMapping("reminder")
    public String reminder(Model model, HttpSession session) {
        if(!userAuthenticator.userIsAdmin(session)){
            return REDIRECT;
        }
        log.info("get reminder action started...");
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        if (showConfirmation) {
            showConfirmation(model);
        }

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
            confirmation("SMS Reminder blev sendt.");
        } else{
            log.info("sms reminder failed to sent...");
            confirmation("Der skete en fejl ved afsendelse af reminder.");
        }

        return REDIRECT + REMINDER;
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
        if (showConfirmation) {
            showConfirmation(model);
        }

        model.addAttribute("newsletter", new Newsletter());
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
            confirmation("Nyhedsbrev blev sendt.");
        } else{
            log.info("sms newsletter failed to sent...");
            confirmation("Der skete en fejl ved afsendelse af nyhedsbrev.");
        }

        return REDIRECT + NEWSLETTER;
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
            confirmation("Test nyhedsbrev blev sendt.");
        } else{
            log.info("sms reminder failed to sent...");
            confirmation("Der skete en fejl ved afsendelse af nyhedsbrev.");
        }

        return REDIRECT + NEWSLETTER;
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
    @PostMapping ("/startAutoReminder")
    public String startAutoReminder(HttpSession session) { //Manually starts the autoReminder
        if(!userAuthenticator.userIsAdmin(session)){
            return REDIRECT;
        }
        sMSServiceImpl.initiateAutoReminder("Initiate");
        return REDIRECT + REMINDER;
    }

    //Asbjørn
    @PostMapping ("/stopAutoReminder")
    public String stopAutoReminde(Model model, HttpSession session){ //Manually stops the autoReminder
        if(!userAuthenticator.userIsAdmin(session)){
            return REDIRECT;
        }
        sMSServiceImpl.initiateAutoReminder("cancel");
        return REDIRECT + REMINDER;
    }
}

package com.firstyearproject.salontina.Controllers;

import com.firstyearproject.salontina.Services.SMSServiceImpl;
import com.firstyearproject.salontina.Models.Newsletter;
import com.firstyearproject.salontina.Services.UserServiceImpl;
import com.firstyearproject.salontina.Models.User;
import com.firstyearproject.salontina.Services.ProductServiceImpl;
import com.firstyearproject.salontina.Models.Item;
import com.firstyearproject.salontina.Models.Treatment;
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

public class BOController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String NEWSLETTER = "newsletter";
    private String REMINDER = "reminder";
    private String REDIRECTNEWSLETTER = "redirect:/" + NEWSLETTER;
    private String REDIRECTREMINDER = "redirect:/" + REMINDER;

    private boolean taskResult = false;  
  
    @Autowired
    SMSServiceImpl sMSServiceImpl;
  
    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    ProductServiceImpl productServiceImpl;

    private boolean showConfirmation = false;
    private String confirmationText = "";

    //Luca
    @GetMapping("reminder")
    public String reminder(Model model, HttpSession session){
        log.info("get reminder action started...");

        if(showConfirmation){
            model.addAttribute("showconfirmation", true);
            model.addAttribute("confirmationtext", confirmationText);
            showConfirmation = false;
        }

        return REMINDER;
    }

    @GetMapping("sendreminder")
    public String sendreminder(Model model, HttpSession session){
        log.info("post sendreminder action started...");

        if(sMSServiceImpl.sendReminder()){
            log.info("sms reminder sent successfully...");
            showConfirmation = true;
            confirmationText = "SMS Reminder blev sendt.";
        } else{
            log.info("sms reminder failed to sent...");
            showConfirmation = true;
            confirmationText = "Der skete en fejl ved afsendelse af reminder.";
        }

        return REDIRECTREMINDER;
    }

    //Luca
    @GetMapping("newsletter")
    public String newsletter(Model model, HttpSession session){
        log.info("get newsletter action started...");

        if(showConfirmation){
            model.addAttribute("showconfirmation", true);
            model.addAttribute("confirmationtext", confirmationText);
            showConfirmation = false;
        }

        model.addAttribute("newsletter", new Newsletter());
        return NEWSLETTER;
    }

    //Luca
    @PostMapping("sendnewsletter")
    public String sendNewsletter(Model model, HttpSession session, @ModelAttribute Newsletter newsletter){
        log.info("post newsletter action started...");

        if(sMSServiceImpl.sendNewsletter(newsletter.getText())){
            log.info("newsletter was successfully sent...");

            showConfirmation = true;
            confirmationText = "Nyhedsbrev blev sendt.";
        } else{
            log.info("sms newsletter failed to sent...");
            showConfirmation = true;
            confirmationText = "Der skete en fejl ved afsendelse af nyhedsbrev.";
        }

        return REDIRECTNEWSLETTER;
    }

    //Luca
    @PostMapping("sendtestnewsletter")
    public String sendTestNewsletter(Model model, HttpSession session,
                                     @ModelAttribute Newsletter newsletter){
        log.info("post newsletter action started...");

        if(sMSServiceImpl.sendNewsletterTest(newsletter.getTestNumber(), newsletter.getText())){
            log.info("newsletter was successfully sent...");

            showConfirmation = true;
            confirmationText = "Test nyhedsbrev blev sendt.";
        } else{
            log.info("sms reminder failed to sent...");
            showConfirmation = true;
            confirmationText = "Der skete en fejl ved afsendelse af nyhedsbrev.";
        }

        return REDIRECTNEWSLETTER;
    }

    //Jonathan
    @GetMapping("/register")
    public String createUser(Model model) {
        model.addAttribute("userToBeRegistered", new User());
        return "register";
    }

    //Jonathan
    @PostMapping("/register")
    public String createUser(@ModelAttribute User user) {
        userServiceImpl.addUser(user);
    return "redirect:/login";
    }

    //Jonathan
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("userToBeRegistered", new User());
        return "login";
    }

    //Asbjørn
    @GetMapping ("/")
    public String index (Model model) {
        return "index";
    }

    //Asbjørn
    @GetMapping ("/createProduct")
    public String createProduct (Model model, HttpSession session) {
        return "createProduct";
    }

    //Asbjørn
    @GetMapping ("/createItem")
    public String createItem (Model model, HttpSession session) {
        model.addAttribute("item", new Item());
        return "createItem";
    }

    //Asbjørn
    @PostMapping ("/createItem")
    public String createItem (@ModelAttribute Item item, Model model, HttpSession session) {
        taskResult = productServiceImpl.createItem(item);
        if (taskResult) {
            return "redirect:/";
        } else {
            return "createItem";
        }
    }

    //Asbjørn
    @GetMapping ("/createTreatment")
    public String createTreatment (Model model, HttpSession session) {
        model.addAttribute("treatment", new Treatment());
        return "createTreatment";
    }

    //Asbjørn
    @PostMapping ("/createTreatment")
    public String createTreatment (@ModelAttribute Treatment treatment, Model model, HttpSession session) {
        taskResult = productServiceImpl.createTreatment(treatment);
        if (taskResult) {
            return "redirect:/";
        } else {
            return "createTreatment";
        }
    }
}
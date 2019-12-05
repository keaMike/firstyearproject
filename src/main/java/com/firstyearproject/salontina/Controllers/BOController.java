package com.firstyearproject.salontina.Controllers;

import com.firstyearproject.salontina.Models.*;
import com.firstyearproject.salontina.Services.BookingServiceImpl;
import com.firstyearproject.salontina.Services.SMSServiceImpl;
import com.firstyearproject.salontina.Services.UserServiceImpl;
import com.firstyearproject.salontina.Services.ProductServiceImpl;
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
import java.util.ArrayList;
import java.util.List;

@Controller
public class BOController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String INDEX = "index";
    private String NEWSLETTER = "newsletter";
    private String REMINDER = "reminder";
    private String CREATEPRODUCT = "createProduct";
    private String CREATEITEM = "createItem";
    private String CREATETREATMENT = "createTreatment";
    private String CONTROLPANEL = "controlpanel";
    private String DISPLAYPRODUCTS = "displayProducts";
    private String EDITTREATMENT = "editTreatment";
    private String EDITITEM = "editItem";
    private String ALLUSERS = "allusers";
    private String EDITUSERHISTORY = "edituserhistory";
    private String REDIRECT = "redirect:/";

    private boolean taskResult = false;
    private boolean showConfirmation = false;
    private String confirmationText = "";

    @Autowired
    SMSServiceImpl sMSServiceImpl;

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    ProductServiceImpl productServiceImpl;

    @Autowired
    BookingServiceImpl bookingServiceImpl;


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
        log.info("get reminder action started...");

        if (showConfirmation) {
            showConfirmation(model);
        }

        return REMINDER;
    }

    //Luca
    @GetMapping("sendreminder")
    public String sendreminder(Model model, HttpSession session) {
        log.info("post sendreminder action started...");

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
        log.info("get newsletter action started...");

        if (showConfirmation) {
            showConfirmation(model);
        }

        model.addAttribute("newsletter", new Newsletter());
        return NEWSLETTER;
    }

    //Luca
    @PostMapping("sendnewsletter")
    public String sendNewsletter(Model model, HttpSession session, @ModelAttribute Newsletter newsletter) {
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

    //Asbjørn
    @GetMapping("/")
    public String index(Model model) {
        return INDEX;
    }

    //Asbjørn
    @GetMapping("/createproduct")
    public String createProduct(Model model, HttpSession session) {
        return CREATEPRODUCT;
    }

    //Asbjørn
    @GetMapping("/createitem")
    public String createItem(Model model, HttpSession session) {
        model.addAttribute("item", new Item());
        return CREATEITEM;
    }

    //Asbjørn
    @PostMapping("/createitem")
    public String createItem(@ModelAttribute Item item, Model model, HttpSession session) {
        taskResult = productServiceImpl.createItem(item);
        if (taskResult) {
            confirmation(item.getProductName() + " er blevet oprettet i systemet");
            return REDIRECT;
        } else {
            confirmation("Produktet kunne ikke oprettes i systemet");
            return CREATEITEM;
        }
    }

    //Asbjørn
    @GetMapping("/createtreatment")
    public String createTreatment(Model model, HttpSession session) {
        model.addAttribute("treatment", new Treatment());
        return CREATETREATMENT;
    }

    //Asbjørn
    @PostMapping("/createtreatment")
    public String createTreatment(@ModelAttribute Treatment treatment, Model model, HttpSession session) {
        taskResult = productServiceImpl.createTreatment(treatment);
        if (taskResult) {
            confirmation(treatment.getProductName() + " er blevet oprettet som behandling i systemet");
            return REDIRECT;
        } else {
            confirmation("Behandlingen kunne ikke oprettes i systemet");
            return CREATETREATMENT;
        }
    }

    //Mike
    @GetMapping("/controlpanel")
    public String controlpanel() {
        return CONTROLPANEL;
    }
  
    //Asbjørn
    @GetMapping ("/displayproducts")
    public String displayProducts (Model model, HttpSession session) {
        model.addAttribute("treatments", productServiceImpl.createTreatmentArrayList());
        model.addAttribute("items", productServiceImpl.createItemArrayList());

        return DISPLAYPRODUCTS;
    }

    //Asbjørn
    @GetMapping ("/edittreatment/{id}")
    public String editTreatment (@PathVariable ("id") int id, Model model, HttpSession session) {
        model.addAttribute("treatments", productServiceImpl.getTreatment(id));
        return EDITTREATMENT;
    }

    //Asbjørn
    @PostMapping ("/edittreatment")
    public String editTreatment (@ModelAttribute Treatment treatment) {
        taskResult = productServiceImpl.editTreatment(treatment);
        if (taskResult) {
            confirmation("Information på behandlingen: " + treatment.getProductName() + " er blevet ændret i systemet");
            return REDIRECT;
        } else {
            confirmation("Behandlingens information kunne IKKE ændres i systemet");
            return DISPLAYPRODUCTS;
        }
    }

    //Asbjørn
    @GetMapping ("/edititem/{id}")
    public String editItem (@PathVariable ("id") int id, Model model, HttpSession session) {
        model.addAttribute("items", productServiceImpl.getTreatment(id));
        return EDITITEM;
    }

    //Asbjørn
    @PostMapping ("/edititem")
    public String editItem (@ModelAttribute Item item) {
        taskResult = productServiceImpl.editItem(item);
        if (taskResult) {
            confirmation("Information på " + item.getProductName() + " er blevet ændret i systemet");
            return REDIRECT;
        } else {
            confirmation("Produktets information kunne IKKE ændres i systemet");
            return DISPLAYPRODUCTS;
        }
    }

    //Mike
    @GetMapping("/userlist")
    public String userList(Model model, HttpSession session) {
        List<User> users = userServiceImpl.getAllUsers();
        model.addAttribute("users", users);
        return ALLUSERS;
    }

    //Mike
    @GetMapping("/edituserhistory/{userid}")
    public String editUserHistory(@PathVariable int userid, Model model, HttpSession session) {
        User user = userServiceImpl.getUserById(userid);
        user.setUserHistory(bookingServiceImpl.getBookingList(user.getUserId()));
        model.addAttribute("user", user);
        return EDITUSERHISTORY;
    }

    //Mike
    @PostMapping("/saveuserhistory")
    public String saveUserHistory(@ModelAttribute User user) {
        taskResult = userServiceImpl.editUserHistory(user);
        if (taskResult) {
            return EDITUSERHISTORY;
        } else {
            return EDITUSERHISTORY + user.getUserId();
        }
    }
}

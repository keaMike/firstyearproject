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

    private String NEWSLETTER = "newsletter";
    private String REMINDER = "reminder";
    private String NEWSLETTERORREMINDER = "newsletterOrReminder";
    private String CREATEPRODUCT = "createProduct";
    private String CREATEITEM = "createItem";
    private String CREATETREATMENT = "createTreatment";
    private String CONTROLPANEL = "controlpanel";
    private String DISPLAYPRODUCTS = "displayProducts";
    private String EDITPRODUCT = "editproduct";
    private String EDITTREATMENT = "editTreatment";
    private String EDITITEM = "editItem";
    private String ALLUSERS = "allusers";
    private String EDITUSERHISTORY = "edituserhistory";
    private String REDIRECT = "redirect:/";

    private boolean taskResult = false;

    private boolean showConfirmation = false;
    private String confirmationText = "";

    private ArrayList<Item> itemArrayList = new ArrayList<>();
    private ArrayList<Treatment> treatmentArrayList = new ArrayList<>();

    @Autowired
    SMSServiceImpl sMSServiceImpl;

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    ProductServiceImpl productServiceImpl;

    @Autowired
    BookingServiceImpl bookingServiceImpl;


    //Luca
    public void confirmation(String text){
        showConfirmation = true;
        confirmationText = text;
    }

    //Luca
    public void showConfirmation(Model model){
        model.addAttribute("showconfirmation", true);
        model.addAttribute("confirmationtext", confirmationText);
        showConfirmation = false;
    }

    //Luca
    @GetMapping("reminder")
    public String reminder(Model model, HttpSession session) {
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

    //Mike
    @GetMapping("/sendbesked")
    public String sendMessage(Model model, HttpSession session) {
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        return NEWSLETTERORREMINDER;
    }

    //Asbjørn
    @GetMapping("/createProduct")
    public String createProduct(Model model, HttpSession session) {
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        return CREATEPRODUCT;
    }

    //Asbjørn
    @GetMapping("/createItem")
    public String createItem(Model model, HttpSession session) {
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("item", new Item());
        return CREATEITEM;
    }

    //Asbjørn
    @PostMapping("/createItem")
    public String createItem(@ModelAttribute Item item, Model model, HttpSession session) {
        taskResult = productServiceImpl.createItem(item);
        if (taskResult) {
            return REDIRECT;
        } else {
            return CREATEITEM;
        }
    }

    //Asbjørn
    @GetMapping("/createTreatment")
    public String createTreatment(Model model, HttpSession session) {
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("treatment", new Treatment());
        return CREATETREATMENT;
    }

    //Asbjørn
    @PostMapping("/createTreatment")
    public String createTreatment(@ModelAttribute Treatment treatment, Model model, HttpSession session) {
        taskResult = productServiceImpl.createTreatment(treatment);
        if (taskResult) {
            return REDIRECT;
        } else {
            return CREATETREATMENT;
        }
    }

    //Mike
    @GetMapping("/kontrolpanel")
    public String controlpanel(Model model, HttpSession session) {
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        return CONTROLPANEL;
    }
  
    //Asbjørn
    @GetMapping ("/behandlinger")
    public String displayTreatments (Model model, HttpSession session) {
        taskResult = productServiceImpl.createProductArrayLists(itemArrayList, treatmentArrayList);
        if(session.getAttribute("user") != null) {
            User user = (User)session.getAttribute("user");
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", new User());
        }
        model.addAttribute("loginToken", new LoginToken());
        model.addAttribute("treatments", treatmentArrayList);
        model.addAttribute("items", itemArrayList);
        model.addAttribute("showTreatments", true);
        model.addAttribute("showProducts", false);
        return DISPLAYPRODUCTS;
    }

    //Asbjørn
    @GetMapping ("/produkter")
    public String displayProducts (Model model, HttpSession session) {
        taskResult = productServiceImpl.createProductArrayLists(itemArrayList, treatmentArrayList);
        if(session.getAttribute("user") != null) {
            User user = (User)session.getAttribute("user");
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", new User());
        }
        model.addAttribute("loginToken", new LoginToken());
        model.addAttribute("treatments", treatmentArrayList);
        model.addAttribute("items", itemArrayList);
        model.addAttribute("showProducts", true);
        model.addAttribute("showTreatments", false);
        return DISPLAYPRODUCTS;
    }

    //Mike
    @GetMapping("/editProduct")
    public String editProduct(Model model, HttpSession session) {
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        return EDITPRODUCT;
    }

    //Asbjørn
    @GetMapping ("/editTreatment/{id}")
    public String editTreatment (@PathVariable ("id") int id, Model model, HttpSession session) {
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("treatments", treatmentArrayList.get(id-1));
        return EDITTREATMENT;
    }

    //Asbjørn
    @PostMapping ("/editTreatment")
    public String editTreatment (@ModelAttribute Treatment treatment) {
        taskResult = productServiceImpl.editTreatment(treatment);
        if (taskResult) {
            return REDIRECT;
        } else {
            return DISPLAYPRODUCTS;
        }
    }

    //Asbjørn
    @GetMapping ("/editItem/{id}")
    public String editItem (@PathVariable ("id") int id, Model model, HttpSession session) {
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("items", itemArrayList.get(id-1));
        return EDITITEM;
    }

    //Asbjørn
    @PostMapping ("/editItem")
    public String editItem (@ModelAttribute Item item) {
        taskResult = productServiceImpl.editItem(item);
        if (taskResult) {
            return REDIRECT;
        } else {
            return DISPLAYPRODUCTS;
        }
    }
    //Mike
    @GetMapping("/edituserhistory")
    public String userList(Model model, HttpSession session) {
        User user = (User)session.getAttribute("user");
        List<User> users = userServiceImpl.getAllUsers();
        model.addAttribute("user", user);
        model.addAttribute("users", users);
        return ALLUSERS;
    }

    //Mike
    @GetMapping("/edituserhistory/{userid}")
    public String editUserHistory(@PathVariable int userid, Model model, HttpSession session) {
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        User editedUser = userServiceImpl.getUserById(userid);
        user.setUserHistory(bookingServiceImpl.getBookingList(user.getUserId()));
        model.addAttribute("editedUser", editedUser);
        return EDITUSERHISTORY;
    }

    //Mike
    @PostMapping("/edituserhistory")
    public String saveUserHistory(@ModelAttribute User user) {
        taskResult = userServiceImpl.editUserHistory(user);
        if (taskResult) {
            return EDITUSERHISTORY;
        } else {
            return EDITUSERHISTORY + user.getUserId();
        }
    }
}

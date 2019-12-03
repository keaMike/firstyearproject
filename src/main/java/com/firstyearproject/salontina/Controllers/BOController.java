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
    private String REDIRECTNEWSLETTER = "redirect:/" + NEWSLETTER;
    private boolean taskResult = false;
    private ArrayList<Item> itemArrayList = new ArrayList<>();
    private ArrayList<Treatment> treatmentArrayList = new ArrayList<>();
    private String REDIRECTREMINDER = "redirect:/" + REMINDER;
    private boolean taskResult = false;    
  
    @Autowired
    SMSServiceImpl sMSServiceImpl;

    @Autowired
    UserServiceImpl userServiceImpl;
    @Autowired
    ProductServiceImpl productServiceImpl;

    @Autowired
    BookingServiceImpl bookingServiceImpl;

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

        if(smsServiceImpl.sendReminder()){
            log.info("sms reminder sent successfully...");
            showConfirmation = true;
            confirmationText = "SMS Reminder blev sendt.";
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

        if(smsServiceImpl.sendNewsletter(newsletter.getText())){
            log.info("newsletter was successfully sent...");

            showConfirmation = true;
            confirmationText = "Nyhedsbrev blev sendt.";
        }
        return REDIRECTNEWSLETTER;
    }

    //Luca
    @PostMapping("sendtestnewsletter")
    public String sendTestNewsletter(Model model, HttpSession session,
                                     @ModelAttribute Newsletter newsletter){
        log.info("post newsletter action started...");

        if(smsServiceImpl.sendNewsletterTest(newsletter.getTestNumber(), newsletter.getText())){
            log.info("newsletter was successfully sent...");

            showConfirmation = true;
            confirmationText = "Test nyhedsbrev blev sendt.";
        }
        return REDIRECTNEWSLETTER;
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
  
    //Asbjørn
    @GetMapping ("/displayProducts")
    public String displayProducts (Model model, HttpSession session) {
        taskResult = productServiceImpl.createProductArrayLists(itemArrayList, treatmentArrayList);
        model.addAttribute("treatments", treatmentArrayList);
        model.addAttribute("items", itemArrayList);

        return "displayProducts";
    }

    //Asbjørn
    @GetMapping ("/editTreatment/{id}")
    public String editTreatment (@PathVariable ("id") int id, Model model, HttpSession session) {
        model.addAttribute("treatments", treatmentArrayList.get(id-1));
        return "editTreatment";
    }

    //Asbjørn
    @PostMapping ("/editTreatment")
    public String editTreatment (@ModelAttribute Treatment treatment) {
        taskResult = productServiceImpl.editTreatment(treatment);
        if (taskResult) {
            return "redirect:/";
        } else {
            return "displayProduct";
        }
    }

    //Asbjørn
    @GetMapping ("/editItem/{id}")
    public String editItem (@PathVariable ("id") int id, Model model, HttpSession session) {
        model.addAttribute("items", itemArrayList.get(id-1));
        return "editItem";
    }

    //Asbjørn
    @PostMapping ("/editItem")
    public String editItem (@ModelAttribute Item item) {
        taskResult = productServiceImpl.editItem(item);
        if (taskResult) {
            return "redirect:/";
        } else {
            return "displayProduct";
        }
    }
    //Mike
    @GetMapping("/edituserhistory")
    public String userList(Model model, HttpSession session) {
        List<User> users = userServiceImpl.getAllUsers();
        model.addAttribute("users", users);
        return "allusers";
    }

    //Mike
    @GetMapping("/edituserhistory/{userid}")
    public String editUserHistory(@PathVariable int userid, Model model, HttpSession session) {
        User user = userServiceImpl.getUserById(userid);
        List<Booking> bookings = bookingServiceImpl.getBookingList(user.getUserId());
        model.addAttribute("user", user);
        model.addAttribute("bookings", bookings);
        return "edituserhistory";
    }

    //Mike
    @PostMapping("/edituserhistory")
    public String saveUserHistory(@ModelAttribute User user) {
        taskResult = userServiceImpl.editUserHistory(user);
        if (taskResult) {
            return "redirect:/";
        } else {
            return "edituserhistory";
        }
    }
}
package com.firstyearproject.salontina.Controllers;

import com.firstyearproject.salontina.Models.*;
import com.firstyearproject.salontina.Services.*;
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
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Controller
public class FOController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String INDEX = "index";
    private String REDIRECT = "redirect:/";
    private String LOGIN = "login";
    private String REGISTER = "register";
    private String REDIGERBRUGER = "redigerbruger";
    private String USERPROFILE = "userprofile";
    private String MYBOOKINGS = "mybookings";
    private String MYPROFILE = "myprofile";
    private String CONTACT = "contact";
    private String DISPLAYPRODUCTS = "displayProducts";
    private String MINEBOOKINGS = "minebookings";
    private String VÆLGTREATMENT = "vælgtreatment";
    private String VÆLGTID = "vælgtid";
    private String BOOKINGCONFIRMATION = "bookingconfirmation";

    private boolean taskResult = false;
    private boolean showConfirmation = false;
    private String confirmationText = "";

    private ArrayList<Item> itemArrayList = new ArrayList<>();
    private ArrayList<Treatment> treatmentArrayList = new ArrayList<>();


    @Autowired
    UserServiceImpl userService;

    @Autowired
    BookingServiceImpl bookingService;

    @Autowired
    ProductServiceImpl productService;

    @Autowired
    SMSServiceImpl smsService;

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
    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        smsService.initiateAutoReminder("Initiate"); //Might not be appropriate place for this method

        if(session.getAttribute("user") != null) {
            User user = (User)session.getAttribute("user");
            model.addAttribute("user", user);
            log.info(user.toString());
        } else {
            model.addAttribute("user", new User());
            model.addAttribute("loginToken", new LoginToken());
        }
        return INDEX;
    }

    //Luca
    @PostMapping("/")
    public String login(Model model, HttpSession session, @ModelAttribute LoginToken loginToken) {
        User user = userService.authenticateUser(loginToken);

        if(user != null){
            session.setAttribute("user", user);

            return REDIRECT;
        }
        return INDEX;
    }

    //Jonathan
    @GetMapping("/register")
    public String createUser(Model model) {
        model.addAttribute("userToBeRegistered", new User());
        return REGISTER;
    }
    //Jonathan
    @PostMapping("/register")
    public String createUser(@ModelAttribute User user) {
        userService.addUser(user);
        return REDIRECT + LOGIN;
    }
    //Jonathan
    @GetMapping("/redigerbruger")
    public String redigerUser(HttpSession session, Model model) {
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        return REDIGERBRUGER;
    }
    //Jonathan
    @PostMapping("/redigerbruger")
    public String redigerUser(@ModelAttribute User user) {
        userService.editUser(user);
        return REDIRECT + USERPROFILE;
    }

    @GetMapping("userprofile")
    public String userprofile(Model model, HttpSession session) {
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        return USERPROFILE;
    }

    //Mike
    @GetMapping("/sletbruger/{userid}")
    public String deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
        return REDIRECT + REDIGERBRUGER;
    }

    //Mike
    @GetMapping("/minebookings")
    public String userbookings(Model model, HttpSession session) {
        User user = (User)session.getAttribute("user");
        user.setUserHistory(bookingService.getBookingList(user.getUserId()));
        model.addAttribute("user", user);
        return MYBOOKINGS;
    }

    //Mike
    @GetMapping("/sletbooking/{bookingid}")
    public String deleteuserbooking(@PathVariable int bookingid) {
        taskResult = bookingService.deleteBooking(bookingid);
        if (taskResult) {
            confirmation("Din booking er blevet slettet");
            return REDIRECT + MINEBOOKINGS;
        } else {
            confirmation("Din booking kunne ikke slettes. Prøv igen på et senere tidspunkt");
            return MYBOOKINGS;
        }
    }

    //Mike
    @GetMapping("/minprofil")
    public String myprofil(Model model, HttpSession session) {
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        return MYPROFILE;
    }

    //Mike
    @GetMapping("/kontakt")
    public String contact(Model model, HttpSession session) {
        if(session.getAttribute("user") != null) {
            User user = (User)session.getAttribute("user");
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", new User());
            model.addAttribute("loginToken", new LoginToken());
        }
        return CONTACT;
    }

    //Jonathan

    @GetMapping("vælgtreatment")
    public String vælgBooking(Model model) {
        model.addAttribute("booking", new Booking());

        productService.createProductArrayLists(itemArrayList, treatmentArrayList);

        model.addAttribute("treatmentList", treatmentArrayList);
        return "chooseBookingTreatment";
    }

    //Jonathan & Luca
    @GetMapping("vælgtid/{treatmentId}")
    public String vælgTid(HttpSession session, Model model, @PathVariable int treatmentId) {
        Date date = new Date(Calendar.getInstance().getTimeInMillis());

        Booking booking = new Booking();
        booking.setBookingTreatmentList(new ArrayList<>());
        booking.getBookingTreatmentList().add(productService.getTreatment(treatmentId));
        booking.setBookingDate(date);
        //TODO set userId from httpsession user

        session.setAttribute("booking", booking);

        List<Booking> bookingList = bookingService.getBookingList(date.toString());

        model.addAttribute("bookingList", bookingList);
        return "chooseBookingTime";
    }

    //Jonathan & Luca
    @GetMapping("bookingconfirmation/{time}")
    public String bookingConfirmation(HttpSession session, Model model, @PathVariable String time) {
        Booking booking = (Booking) session.getAttribute("booking");

        booking.setBookingTime(time);

        System.out.println(booking);

        bookingService.addBooking(booking);

        model.addAttribute("booking", booking);
        return BOOKINGCONFIRMATION;

    }

    //Asbjørn
    @PostMapping("subscribeNewsletter")
    public String subscribeNewsletter (@ModelAttribute User user) {
        taskResult = userService.subscribeNewsletter(user.getUserId());
        if (taskResult) {
            confirmation("Du er blevet tilmeldt nyhedsbrevet");
            return "redigerbruger";
        }
        confirmation("Vi kunne IKKE tilmelde dig nyhedsbrevet. Prøv igen senere");
        return "redirect:/";
    }

    //Asbjørn
    @PostMapping("unsubscribeNewsletter")
    public String unsubscribeNewsletter (@ModelAttribute User user) {
        taskResult = userService.unsubscribeNewsletter(user.getUserId());
        if (taskResult) {
            confirmation("Du er blevet afmeldt nyhedsbrevet");
            return "redigerbruger";
        }
        confirmation("Vi kunne IKKE afmelde dig nyhedsbrevet. Prøv igen senere");
        return "redigerbruger";
    }
}

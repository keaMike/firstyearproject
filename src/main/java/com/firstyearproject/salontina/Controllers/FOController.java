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
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
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

    private ArrayList<Item> itemArrayList = new ArrayList<>();
    private ArrayList<Treatment> treatmentArrayList = new ArrayList<>();

    @Autowired
    UserServiceImpl userService;

    @Autowired
    BookingServiceImpl bookingService;

    @Autowired
    ProductServiceImpl productService;

    //Mike
    @GetMapping("/")
    public String index(Model model, HttpSession session) {
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
            return REDIRECT + MINEBOOKINGS;
        } else {
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
        return VÆLGTREATMENT;
    }

    //Jonathan
    @PostMapping("vælgtreatment")
    public String vælgBooking(HttpSession httpSession, @ModelAttribute Booking booking) {
        httpSession.setAttribute("booking", booking);//Sender Booking op i HttpSession
        return VÆLGTID;
    }

    //Jonathan
    @GetMapping("vælgtid")
    public String vælgTid(HttpSession httpSession, Model model) {
        Booking booking = (Booking) httpSession.getAttribute("booking");  //Henter Booking fra HttpSession
        model.addAttribute("booking", booking);
        return VÆLGTID;
    }

    //Jonathan
    @PostMapping("vælgtid")
    public String vælgTid(HttpSession httpSession, @ModelAttribute Booking booking) {
        httpSession.setAttribute("booking", booking); //Sender Booking op i HttpSession
        bookingService.addBooking(booking); //Gemmer booking
        return BOOKINGCONFIRMATION;
    }

    //Jonathan
    @GetMapping("bookingconfirmation")
    public String bookingConfirmation(HttpSession httpSession, Model model) {
        Booking booking = (Booking) httpSession.getAttribute("booking"); //Henter Booking fra HttpSession
        model.addAttribute("booking", booking);
        return BOOKINGCONFIRMATION;

    }

}

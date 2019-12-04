package com.firstyearproject.salontina.Controllers;

import com.firstyearproject.salontina.Models.Booking;
import com.firstyearproject.salontina.Models.LoginToken;
import com.firstyearproject.salontina.Models.User;
import com.firstyearproject.salontina.Models.Item;
import com.firstyearproject.salontina.Models.Treatment;
import com.firstyearproject.salontina.Repositories.UserRepoImpl;
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
    //TODO logging in mappings

    private String REDIRECT = "redirect:/";
    private String LOGIN = "login";
    private String REGISTER = "register";
    private String REDIGERBRUGER = "redigerbruger";
    private String USERPROFILE = "userprofile";
    private String MYBOOKINGS = "mybookings";
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

    @GetMapping("/login")
    public String login(Model model, HttpSession session) {
        model.addAttribute("loginToken", new LoginToken());
        return LOGIN;
    }

    @PostMapping("/login")
    public String login(Model model, HttpSession session, @ModelAttribute LoginToken loginToken) {
        User user = userService.authenticateUser(loginToken);

        if(user != null){
            session.setAttribute("user", user);

            return REDIRECT + REGISTER;
        }
        return LOGIN;
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
        User user =  new User(); //(User)session.getAttribute("user");
        model.addAttribute("userToBeEdited", user);
        return REDIGERBRUGER;
    }
    //Jonathan
    @PostMapping("/redigerbruger")
    public String redigerUser(@ModelAttribute User user) {
        userService.editUser(user);
        return REDIRECT + USERPROFILE;
    }

    @GetMapping("userprofile")
    public String userprofile(Model model) {
        model.addAttribute("userToBeViewed", new User());
        return USERPROFILE;
    }

    //Mike
    @GetMapping("/sletbruger/{userid}")
    public String deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
        return REDIRECT + REDIGERBRUGER;
    }

    @GetMapping("/minebookings")
    public String userbookings(Model model, HttpSession session) {
        //Test user
        User user = userService.getUserById(3);
        List<Booking> bookings = bookingService.getBookingList(user.getUserId());
        model.addAttribute("bookings", bookings);
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

    //Jonathan & Luca
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
        userService.subscribeNewsletter(user.getUserId());
        return "redirect:/";
    }

    //Asbjørn
    @PostMapping("unsubscribeNewsletter")
    public String unsubscribeNewsletter (@ModelAttribute User user) {
        userService.unsubscribeNewsletter(user.getUserId());
        return "redirect:/";
    }

    @GetMapping ("/kontakt")
    public String kontakt () {
        return "kontakt";
    }
}

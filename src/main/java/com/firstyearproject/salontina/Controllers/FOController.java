package com.firstyearproject.salontina.Controllers;

import com.firstyearproject.salontina.Models.Booking;
import com.firstyearproject.salontina.Models.LoginToken;
import com.firstyearproject.salontina.Models.User;
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
import java.util.List;

@Controller
public class FOController {


    private boolean taskResult = false;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String LANDINGPAGE = "landingpage";
    private String ADDBOOKING = "addbooking";
    private String REDIRECT = "redirect:/";
    private String LOGIN = "login";
    private String REGISTER = "register";
    private String REDIRECTREGISTER = "redirect:/" + REGISTER;


    @Autowired
    UserServiceImpl userService;
    @Autowired
    BookingServiceImpl bookingService;

    @Autowired
    ProductServiceImpl productService;

    @Autowired
    BookingService bookingService;

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

            return REDIRECTREGISTER;
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
        return "redirect:/login";
    }
    //Jonathan
    @GetMapping("/redigerbruger")
    public String redigerUser(HttpSession session, Model model) {
        User user =  new User(); //(User)session.getAttribute("user");
        model.addAttribute("userToBeEdited", user);
        return "redigerbruger";
    }
    //Jonathan
    @PostMapping("/redigerbruger")
    public String redigerUser(@ModelAttribute User user) {
        userService.editUser(user);
        return "redirect:/userprofile";
    }

    @GetMapping("userprofile")
    public String userprofile(Model model) {
        model.addAttribute("userToBeViewed", new User());
        return "userprofile";
    }

    //Mike
    @GetMapping("/sletbruger/{userid}")
    public String deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
        return "redirect:/redigerbruger";

    @GetMapping("/minebookings")
    public String userbookings(Model model, HttpSession session) {
        //Test user
        User user = userService.getUserById(3);
        List<Booking> bookings = bookingService.getBookingList(user.getUserId());
        model.addAttribute("bookings", bookings);
        model.addAttribute("user", user);
        return "mybookings";
    }

    //Mike
    @GetMapping("/sletbooking/{bookingid}")
    public String deleteuserbooking(@PathVariable int bookingid) {
        taskResult = bookingService.deleteBooking(bookingid);
        if (taskResult) {
            return "redirect:/minebookings";
        } else {
            return "mybookings";
        }
    }

    //Jonathan
    @GetMapping("vælgtreatment")
    public String vælgBooking(Model model) {
        model.addAttribute("booking", new Booking());
        return "vælgtreatment";
    }
    //Jonathan
    @PostMapping("vælgtreatment")
    public String vælgBooking(HttpSession httpSession, @ModelAttribute Booking booking) {
        httpSession.setAttribute("booking", booking);//Sender Booking op i HttpSession
        return "vælgtid";
    }
    //Jonathan
    @GetMapping("vælgtid")
    public String vælgTid(HttpSession httpSession, Model model) {
        Booking booking = (Booking) httpSession.getAttribute("booking");  //Henter Booking fra HttpSession
        model.addAttribute("booking", booking);
        return "vælgtid";
    }
    //Jonathan
    @PostMapping("vælgtid")
    public String vælgTid(HttpSession httpSession, @ModelAttribute Booking booking) {
        httpSession.setAttribute("booking", booking); //Sender Booking op i HttpSession
        bookingService.addBooking(booking); //Gemmer booking
        return "bookingconfirmation";
    }
    //Jonathan
    @GetMapping("bookingconfirmation")
    public String bookingConfirmation(HttpSession httpSession, Model model) {
        Booking booking = (Booking) httpSession.getAttribute("booking"); //Henter Booking fra HttpSession
        model.addAttribute("booking", booking);
        return "bookingconfirmation";

    }

}

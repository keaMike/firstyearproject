package com.firstyearproject.salontina.Controllers;

import com.firstyearproject.salontina.Models.Booking;
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
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpSession;
import java.awt.print.Book;
import java.util.List;

@Controller
public class FOController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String LANDINGPAGE = "landingpage";
    private String ADDBOOKING = "addbooking";
    private String REDIRECT = "redirect:/";

    @Autowired
    UserServiceImpl userService;

    @Autowired
    ProductServiceImpl productService;

    @Autowired
    BookingService bookingService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("userToBeRegistered", new User());
        return "login";
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
        userService.addUser(user);
        return "redirect:/login";
    }
    //Jonathan
    @GetMapping("/redigerbruger")
    public String redigerUser(HttpSession session, Model model) {
        User user = (User)session.getAttribute("user");
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

package com.firstyearproject.salontina.Controllers.FrontOfficeControllers;

import com.firstyearproject.salontina.Models.Booking;
import com.firstyearproject.salontina.Models.User;
import com.firstyearproject.salontina.Services.BookingServiceImpl;
import com.firstyearproject.salontina.Services.ProductServiceImpl;
import com.firstyearproject.salontina.Services.UserAuthenticator;
import com.firstyearproject.salontina.Services.UserServiceImpl;
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
public class BookingController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String REDIRECT = "redirect:/";
    private String MYBOOKINGS = "bookings/mybookings";
    private String BOOKINGCONFIRMATION = "bookings/bookingconfirmation";
    private String CHOOSEBOOKINGTREATMENT = "bookings/choosebookingtreatment";
    private String CHOOSEBOOKINGTIME = "bookings/choosebookingtime";

    private boolean taskResult = false;
    private boolean showConfirmation = false;
    private String confirmationText = "";

    @Autowired
    BookingServiceImpl bookingService;

    @Autowired
    ProductServiceImpl productService;

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

    //Mike
    @GetMapping("/mybookings")
    public String userBookings(Model model, HttpSession session) {
        if(!userAuthenticator.userIsUser(session)){
            return REDIRECT;
        }
        User user = (User)session.getAttribute("user");
        if(userAuthenticator.userIsAdmin(session)){
            model.addAttribute("bookings", bookingService.getFutureBookings());
        } else {
            model.addAttribute("bookings", bookingService.getBookingList(user.getUserId()));
        }
        model.addAttribute("user", user);
        return MYBOOKINGS;
    }

    //Mike
    //FIX
    @PostMapping("/deletebooking")
    public String deleteUserBooking(@ModelAttribute Booking booking, HttpSession session) {
        log.info(booking.getBookingId() + "");
        if(!userAuthenticator.userIsUser(session)){
            return REDIRECT;
        }
        taskResult = bookingService.deleteBooking(booking.getBookingId());
        if (taskResult) {
            confirmation("Din booking er blevet slettet");
            return REDIRECT + MYBOOKINGS;
        } else {
            confirmation("Din booking kunne ikke slettes. Prøv igen på et senere tidspunkt");
            return MYBOOKINGS;
        }
    }

    //Jonathan & Luca
    @GetMapping("choosetreatment")
    public String chooseTreatment(Model model, HttpSession session) {
        if(!userAuthenticator.userIsUser(session)){
            return REDIRECT;
        }
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("booking", new Booking());
        model.addAttribute("treatmentList", productService.createTreatmentArrayList());
        return CHOOSEBOOKINGTREATMENT;
    }

    //Jonathan & Luca
    @GetMapping("choosetime/{treatmentId}")
    public String chooseTime(HttpSession session, Model model, @PathVariable int treatmentId) {
        if(!userAuthenticator.userIsUser(session)){
            return REDIRECT;
        }

        User user = (User) session.getAttribute("user");
        Date date = new Date(Calendar.getInstance().getTimeInMillis());

        Booking booking = new Booking();
        booking.setBookingTreatmentList(new ArrayList<>());
        booking.getBookingTreatmentList().add(productService.getTreatment(treatmentId));
        booking.setBookingDate(date);
        booking.setBookingUserId(user.getUserId());

        session.setAttribute("booking", booking);

        List<Booking> bookingList = bookingService.getBookingList(date.toString());

        model.addAttribute("user", user);
        model.addAttribute("bookingList", bookingList);
        return CHOOSEBOOKINGTIME;
    }

    //Jonathan & Luca
    @GetMapping("bookingconfirmation/{time}")
    public String bookingConfirmation(HttpSession session, Model model, @PathVariable String time) {
        if(!userAuthenticator.userIsUser(session)){
            return REDIRECT;
        }
        User user = (User) session.getAttribute("user");

        Booking booking = (Booking) session.getAttribute("booking");

        booking.setBookingTime(time);

        bookingService.addBooking(booking);

        model.addAttribute("user", user);
        model.addAttribute("booking", booking);
        return BOOKINGCONFIRMATION;

    }

}

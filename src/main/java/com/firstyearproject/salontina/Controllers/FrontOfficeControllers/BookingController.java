package com.firstyearproject.salontina.Controllers.FrontOfficeControllers;

import com.firstyearproject.salontina.Models.Booking;
import com.firstyearproject.salontina.Models.User;
import com.firstyearproject.salontina.Services.BookingServiceImpl;
import com.firstyearproject.salontina.Services.ProductServiceImpl;
import com.firstyearproject.salontina.Tools.ConfirmationTool;
import com.firstyearproject.salontina.Tools.SessionLog;
import com.firstyearproject.salontina.Tools.UserAuthenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    private String DELETEBOOKING = "bookings/deletebooking";
    private String BOOKINGCONFIRMATION = "bookings/bookingconfirmation";
    private String CHOOSEBOOKINGTREATMENT = "bookings/choosebookingtreatment";
    private String CHOOSEBOOKINGTIME = "bookings/choosebookingtime";

    private boolean taskResult = false;

    @Autowired
    BookingServiceImpl bookingService;

    @Autowired
    ProductServiceImpl productService;

    @Autowired
    UserAuthenticator userAuthenticator;

    @Autowired
    ConfirmationTool confirmationTool;

    //Mike
    @GetMapping("/mybookings")
    public String userBookings(Model model, HttpSession session) {
        log.info("get mybookings action started..." + SessionLog.sessionId(session));

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
        confirmationTool.showConfirmation(model);
        return MYBOOKINGS;
    }

    //Mike
    @GetMapping("/deletebooking/{bookingId}")
    public String deleteUserBooking(@PathVariable int bookingId, HttpSession session) {
        log.info(" action started..." + SessionLog.sessionId(session));

        if(!userAuthenticator.userIsUser(session)){
            return REDIRECT;
        }
        User user = (User)session.getAttribute("user");
        List<Booking> bookings = bookingService.getBookingList(user.getUserId());
        for(Booking booking : bookings) {
            if(booking.getBookingId() == bookingId) {
                taskResult = bookingService.deleteBooking(bookingId);
                if (taskResult) {
                    confirmationTool.confirmation("Din booking er blevet slettet", ConfirmationTool.success);
                    return REDIRECT + "mybookings";
                } else {
                    confirmationTool.confirmation("Din booking kunne ikke slettes. Prøv igen på et senere tidspunkt", ConfirmationTool.danger);
                    return REDIRECT + "mybookings";
                }
            }
        }
        confirmationTool.confirmation("Du kan ikke slette en booking som ikke er din egen", ConfirmationTool.danger);
        return REDIRECT;
    }

    //Jonathan & Luca
    @GetMapping("choosetreatment")
    public String chooseTreatment(Model model, HttpSession session) {
        log.info("get choosetreatment action started..." + SessionLog.sessionId(session));

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
        log.info("get choosetime action started..." + SessionLog.sessionId(session));

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
        log.info("get bookingconfirmation action started..." + SessionLog.sessionId(session));

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

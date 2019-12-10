package com.firstyearproject.salontina.Controllers.FrontOfficeControllers;

import com.firstyearproject.salontina.Models.Booking;
import com.firstyearproject.salontina.Models.ChooseDate;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.text.SimpleDateFormat;
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
            log.info(SessionLog.NOTLOGGEDIN + SessionLog.sessionId(session));

            return REDIRECT;
        }

        User user = (User) session.getAttribute("user");
        if(userAuthenticator.userIsAdmin(session)){
            log.info("user is logged in as admin... showing future bookings..." + SessionLog.sessionId(session));

            model.addAttribute("bookings", bookingService.getFutureBookings());
        } else {
            log.info("user is logged in... showing bookings..." + SessionLog.sessionId(session));

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
            log.info(SessionLog.NOTLOGGEDIN + SessionLog.sessionId(session));

            return REDIRECT;
        }

        User user = (User) session.getAttribute("user");
        if(bookingService.deleteBooking(bookingService.getBookingList(user.getUserId()), bookingId, userAuthenticator.userIsAdmin(session))){
            log.info("deleted booking: " + bookingId + "..." + SessionLog.sessionId(session));

            confirmationTool.confirmation("Din booking er blevet slettet", ConfirmationTool.success);
            return REDIRECT + "mybookings";
        }
        log.info("could not delete booking..." + SessionLog.sessionId(session));

        confirmationTool.confirmation("Din booking kunne ikke slettes. Prøv igen på et senere tidspunkt", ConfirmationTool.danger);
        return REDIRECT + "mybookings";
    }

    //Jonathan & Luca
    @GetMapping("choosetreatment")
    public String chooseTreatment(Model model, HttpSession session) {
        log.info("get choosetreatment action started..." + SessionLog.sessionId(session));

        if(!userAuthenticator.userIsUser(session)){
            log.info(SessionLog.NOTLOGGEDIN + SessionLog.sessionId(session));

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
            log.info(SessionLog.NOTLOGGEDIN + SessionLog.sessionId(session));

            return REDIRECT;
        }

        User user = (User) session.getAttribute("user");

        Booking booking = new Booking();
        booking.setBookingTreatmentList(new ArrayList<>());
        if(treatmentId != 0){
            booking.getBookingTreatmentList().add(productService.getTreatment(treatmentId));
        }
        booking.setBookingUserId(user.getUserId());

        ChooseDate chooseDate = (ChooseDate) session.getAttribute("choosedate");

        List<Booking> bookingList;

        if(chooseDate == null){
            Date date = new Date(Calendar.getInstance().getTimeInMillis());
            booking.setBookingDate(date);
            bookingList = bookingService.getBookingList(date.toString());
            model.addAttribute("choosedate", new ChooseDate(date.toString()));
        } else {
            booking.setBookingDate(bookingService.parseDateString(chooseDate.getString()));
            bookingList = bookingService.getBookingList(chooseDate.getString());
            model.addAttribute("choosedate", chooseDate);
        }

        session.setAttribute("booking", booking);

        model.addAttribute("user", user);
        model.addAttribute("bookingList", bookingList);
        return CHOOSEBOOKINGTIME;
    }

    @PostMapping("choosetime/changebookingdate")
    public String changebookingdate(Model model, HttpSession session, @ModelAttribute ChooseDate chooseDate){
        log.info("post changebookingtime action started..." + SessionLog.sessionId(session));

        if(chooseDate.getString() != null){
            session.setAttribute("choosedate", chooseDate);
        }

        return REDIRECT + "choosetime/0";
    }

    //Jonathan & Luca
    @GetMapping("bookingconfirmation/{time}")
    public String bookingConfirmation(HttpSession session, Model model, @PathVariable String time) {
        log.info("get bookingconfirmation action started..." + SessionLog.sessionId(session));

        if(!userAuthenticator.userIsUser(session)){
            log.info(SessionLog.NOTLOGGEDIN + SessionLog.sessionId(session));

            return REDIRECT;
        }
        User user = (User) session.getAttribute("user");

        Booking booking = (Booking) session.getAttribute("booking");

        booking.setBookingTime(time);

        if(bookingService.bookingExists(booking)){
            log.info("booking already exists... can not add new booking..." + SessionLog.sessionId(session));

            session.removeAttribute("booking");

            confirmationTool.confirmation("Den valgte tid og dato er allerede booket. Prøv igen.", ConfirmationTool.danger);
            return REDIRECT;
        }

        if(bookingService.addBooking(booking)){
            log.info("added booking: " + booking.getBookingId() + "..." + SessionLog.sessionId(session));

            session.removeAttribute("booking");

            model.addAttribute("user", user);
            model.addAttribute("booking", booking);
            return BOOKINGCONFIRMATION;
        }
        log.info("could not add booking..." + SessionLog.sessionId(session));

        confirmationTool.confirmation("Kunne ikke tilføje din booking. Hvis problemet sker igen, ring til salonen.", ConfirmationTool.danger);
        return REDIRECT;
    }

}

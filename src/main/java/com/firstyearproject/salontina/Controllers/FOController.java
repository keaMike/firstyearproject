package com.firstyearproject.salontina.Controllers;

import com.firstyearproject.salontina.Handlers.BookingHandler;
import com.firstyearproject.salontina.Handlers.ProductHandler;
import com.firstyearproject.salontina.Models.Booking;
import com.firstyearproject.salontina.Models.Treatment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class FOController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String LANDINGPAGE = "landingpage";
    private String ADDBOOKING = "addbooking";
    private String REDIRECT = "redirect:/";

    @Autowired
    BookingHandler bookingHandler;
    @Autowired
    ProductHandler productHandler;

    @GetMapping("/")
    public String landingpage() {
        return LANDINGPAGE;
    }

    @GetMapping("/vælgbehandling")
    public String selectTreatment(Model model) {
        Booking booking = new Booking();
        List treatments = productHandler.getTreatments();

        model.addAttribute("booking", booking);
        model.addAttribute("treatments", treatments);
        return ADDBOOKING;
    }

    @GetMapping("/vælgdato")
    public String selectDate(@ModelAttribute Booking booking, Model model) {
        List availableDates = bookingHandler.getAvailableDates(booking.getBookingTreatmentList());
        log.info(availableDates.toString());
        model.addAttribute("booking", booking);
        model.addAttribute("availableDates", availableDates);
        return ADDBOOKING;
    }

    @GetMapping("vælgtidspunkt")
    public String selectTime() {
        return ADDBOOKING;
    }

    @PostMapping("/vælgbehandling")
    public String addbooking(@ModelAttribute Booking booking) {
        log.info(booking.toString());
        //bookingHandler.addBooking(booking);
        return REDIRECT + LANDINGPAGE;
    }
}

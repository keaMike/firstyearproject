package com.firstyearproject.salontina.Controllers;

import com.firstyearproject.salontina.Models.Booking;
import com.firstyearproject.salontina.Models.User;
import com.firstyearproject.salontina.Services.BookingServiceImpl;
import com.firstyearproject.salontina.Services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class FOController {

    private boolean taskResult = false;

    @Autowired
    UserServiceImpl userService;
    @Autowired
    BookingServiceImpl bookingService;

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

    @GetMapping("/redigerbruger")
    public String redigerUser(HttpSession session, Model model) {
        User user = (User)session.getAttribute("user");
        model.addAttribute("userToBeEdited", user);
        return "redigerbruger";
    }

    @PostMapping("/redigerbruger")
    public String redigerbruger(@ModelAttribute User user) {
        userService.editUser(user);
        return "redirect:/userprofile";
    }

    @GetMapping("userprofile")
    public String userprofile(Model model) {
        model.addAttribute("userToBeViewed", new User());
        return "userprofile";
    }

    //Mike
    @GetMapping("/minebookings")
    public String userbookings(Model model, HttpSession session) {
        //Test user
        User user = userService.getUserById(3);
        List<Booking> bookings = bookingService.getBookingList(user.getUserId());
        model.addAttribute("bookings", bookings);
        model.addAttribute("user", user);
        return "mybookings";
    }

    @GetMapping("/sletbooking/{bookingid}")
    public String deleteuserbooking(@PathVariable int bookingid) {
        taskResult = bookingService.deleteBooking(bookingid);
        if (taskResult) {
            return "redirect:/minebookings";
        } else {
            return "mybookings";
        }
    }

}

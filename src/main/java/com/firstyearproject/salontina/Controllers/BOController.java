package com.firstyearproject.salontina.Controllers;

import com.firstyearproject.salontina.Handlers.UserHandler;
import com.firstyearproject.salontina.Models.User;
import com.firstyearproject.salontina.Handlers.ProductHandler;
import com.firstyearproject.salontina.Models.Item;
import com.firstyearproject.salontina.Models.Product;
import com.firstyearproject.salontina.Models.Treatment;
import com.firstyearproject.salontina.Repositories.ProductRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller

public class BOController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private boolean taskResult = false;  
  
    @Autowired
    UserHandler userHandler;

    @Autowired
    ProductHandler productHandler;

    @GetMapping("/register")
    public String createUser(Model model) {
        model.addAttribute("userToBeRegistered", new User());
        return "register";
    }

    @PostMapping("/register")
    public String createUser(@ModelAttribute User user) {
        userHandler.addUser(user);
    return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("userToBeRegistered", new User());
        return "login";
    }

    @GetMapping ("/")
    public String index (Model model) {
        return "index";
    }

    @GetMapping ("/createProduct")
    public String createProduct (Model model, HttpSession session) {
        return "createProduct";
    }

    @GetMapping ("/createItem")
    public String createItem (Model model, HttpSession session) {
        model.addAttribute("item", new Item());
        return "createItem";
    }

    @PostMapping ("/createItem")
    public String createItem (@ModelAttribute Item item, Model model, HttpSession session) {
        taskResult = productHandler.createItem(item);
        if (taskResult) {
            return "redirect:/";
        } else {
            return "createItem";
        }
    }

    @GetMapping ("/createTreatment")
    public String createTreatment (Model model, HttpSession session) {
        model.addAttribute("treatment", new Treatment());
        return "createTreatment";
    }

    @PostMapping ("/createTreatment")
    public String createTreatment (@ModelAttribute Treatment treatment, Model model, HttpSession session) {
        taskResult = productHandler.createTreatment(treatment);
        if (taskResult) {
            return "redirect:/";
        } else {
            return "createTreatment";
        }
    }
}
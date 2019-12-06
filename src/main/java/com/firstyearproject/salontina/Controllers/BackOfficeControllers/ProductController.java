package com.firstyearproject.salontina.Controllers.BackOfficeControllers;

import com.firstyearproject.salontina.Models.Item;
import com.firstyearproject.salontina.Models.LoginToken;
import com.firstyearproject.salontina.Models.Treatment;
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

import javax.servlet.http.HttpSession;

@Controller
public class ProductController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String CREATEPRODUCT = "products/createProduct";
    private String CREATEITEM = "products/createItem";
    private String CREATETREATMENT = "products/createTreatment";
    private String DISPLAYPRODUCTS = "products/displayProducts";
    private String EDITPRODUCT = "products/editproduct";
    private String EDITTREATMENT = "products/editTreatment";
    private String EDITITEM = "products/editItem";
    private String REDIRECT = "redirect:/";

    private boolean taskResult = false;
    private boolean showConfirmation = false;
    private String confirmationText = "";

    @Autowired
    ProductServiceImpl productServiceImpl;

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

    //Asbjørn
    @GetMapping("/createproduct")
    public String createProduct(Model model, HttpSession session) {
        if(!userAuthenticator.userIsAdmin(session)){
            return REDIRECT;
        }
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        return CREATEPRODUCT;
    }

    //Asbjørn
    @GetMapping("/createitem")
    public String createItem(Model model, HttpSession session) {
        if(!userAuthenticator.userIsAdmin(session)){
            return REDIRECT;
        }
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("item", new Item());
        return CREATEITEM;
    }

    //Asbjørn
    @PostMapping("/createitem")
    public String createItem(@ModelAttribute Item item, HttpSession session) {
        if(!userAuthenticator.userIsAdmin(session)){
            return REDIRECT;
        }
        taskResult = productServiceImpl.createItem(item);
        if (taskResult) {
            confirmation(item.getProductName() + " er blevet oprettet i systemet");
            return REDIRECT;
        } else {
            confirmation("Produktet kunne ikke oprettes i systemet");
            return CREATEITEM;
        }
    }

    //Asbjørn
    @GetMapping("/createtreatment")
    public String createTreatment(Model model, HttpSession session) {
        if(!userAuthenticator.userIsAdmin(session)){
            return REDIRECT;
        }
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("treatment", new Treatment());
        return CREATETREATMENT;
    }

    //Asbjørn
    @PostMapping("/createtreatment")
    public String createTreatment(@ModelAttribute Treatment treatment, HttpSession session) {
        if(!userAuthenticator.userIsAdmin(session)){
            return REDIRECT;
        }
        taskResult = productServiceImpl.createTreatment(treatment);
        if (taskResult) {
            confirmation(treatment.getProductName() + " er blevet oprettet som behandling i systemet");
            return REDIRECT;
        } else {
            confirmation("Behandlingen kunne ikke oprettes i systemet");
            return CREATETREATMENT;
        }
    }

    //Asbjørn
    @GetMapping ("/treatments")
    public String displayTreatments (Model model, HttpSession session) {
        if(session.getAttribute("user") != null) {
            User user = (User)session.getAttribute("user");
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", new User());
        }
        model.addAttribute("loginToken", new LoginToken());
        model.addAttribute("treatments", productServiceImpl.createTreatmentArrayList());
        model.addAttribute("items", productServiceImpl.createItemArrayList());
        model.addAttribute("showTreatments", true);
        model.addAttribute("showProducts", false);
        return DISPLAYPRODUCTS;
    }

    //Asbjørn
    @GetMapping ("/products")
    public String displayProducts (Model model, HttpSession session) {
        if(session.getAttribute("user") != null) {
            User user = (User)session.getAttribute("user");
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", new User());
        }
        model.addAttribute("loginToken", new LoginToken());
        model.addAttribute("treatments", productServiceImpl.createTreatmentArrayList());
        model.addAttribute("items", productServiceImpl.createItemArrayList());
        model.addAttribute("showProducts", true);
        model.addAttribute("showTreatments", false);
        return DISPLAYPRODUCTS;
    }

    //Mike
    @GetMapping("/editproduct")
    public String editProduct(Model model, HttpSession session) {
        if(!userAuthenticator.userIsAdmin(session)){
            return REDIRECT;
        }
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        return EDITPRODUCT;
    }

    //Asbjørn
    @GetMapping ("/edittreatment/{id}")
    public String editTreatment (@PathVariable("id") int id, Model model, HttpSession session) {
        if(!userAuthenticator.userIsAdmin(session)){
            return REDIRECT;
        }
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("treatments", productServiceImpl.getTreatment(id));
        return EDITTREATMENT;
    }

    //Asbjørn
    @PostMapping ("/edittreatment")
    public String editTreatment (@ModelAttribute Treatment treatment, HttpSession session) {
        if(!userAuthenticator.userIsAdmin(session)){
            return REDIRECT;
        }
        taskResult = productServiceImpl.editTreatment(treatment);
        if (taskResult) {
            confirmation("Information på behandlingen: " + treatment.getProductName() + " er blevet ændret i systemet");
            return REDIRECT;
        } else {
            confirmation("Behandlingens information kunne IKKE ændres i systemet");
            return DISPLAYPRODUCTS;
        }
    }

    //Asbjørn
    @GetMapping ("/edititem/{id}")
    public String editItem (@PathVariable ("id") int id, Model model, HttpSession session) {
        if(!userAuthenticator.userIsAdmin(session)){
            return REDIRECT;
        }
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("items", productServiceImpl.getItem(id));
        return EDITITEM;
    }

    //Asbjørn
    @PostMapping ("/edititem")
    public String editItem (@ModelAttribute Item item, HttpSession session) {
        if(!userAuthenticator.userIsAdmin(session)){
            return REDIRECT;
        }
        taskResult = productServiceImpl.editItem(item);
        if (taskResult) {
            confirmation("Information på " + item.getProductName() + " er blevet ændret i systemet");
            return REDIRECT;
        } else {
            confirmation("Produktets information kunne IKKE ændres i systemet");
            return DISPLAYPRODUCTS;
        }
    }

}
package com.firstyearproject.salontina.Controllers.BackOfficeControllers;

import com.firstyearproject.salontina.Controllers.FrontOfficeControllers.UserAccessController;
import com.firstyearproject.salontina.Models.Item;
import com.firstyearproject.salontina.Models.LoginToken;
import com.firstyearproject.salontina.Models.Treatment;
import com.firstyearproject.salontina.Models.User;
import com.firstyearproject.salontina.Services.*;
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
    private String PRODUCTS = "products";
    private String TREATMENTS = "treatments";

    @Autowired
    ProductServiceImpl productServiceImpl;

    @Autowired
    UserAuthenticator userAuthenticator;

    @Autowired
    ConfirmationTool confirmationTool;

    //Asbjørn
    @GetMapping("/createproduct")
    public String createProduct(Model model, HttpSession session) {
        log.info("get createproduct action started..." + SessionLog.sessionId(session));

        if(!userAuthenticator.userIsAdmin(session)){
            log.info(SessionLog.NOTADMIN + SessionLog.sessionId(session));

            return REDIRECT;
        }
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        return CREATEPRODUCT;
    }

    //Asbjørn
    @GetMapping("/createitem")
    public String createItem(Model model, HttpSession session) {
        log.info("get createitem action started..." + SessionLog.sessionId(session));

        if(!userAuthenticator.userIsAdmin(session)){
            log.info(SessionLog.NOTADMIN + SessionLog.sessionId(session));

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
        log.info("post createitem action started..." + SessionLog.sessionId(session));

        if(!userAuthenticator.userIsAdmin(session)){
            log.info(SessionLog.NOTADMIN + SessionLog.sessionId(session));

            return REDIRECT;
        }

        if (productServiceImpl.createItem(item)) {
            log.info("created item: " + item.getProductName() + "..." + SessionLog.sessionId(session));

            confirmationTool.confirmation(item.getProductName() + " er blevet oprettet i systemet", ConfirmationTool.success);
            return REDIRECT + PRODUCTS;
        }
        log.info("could not create item: " + item.getProductName() + "..." + SessionLog.sessionId(session));

        confirmationTool.confirmation("Produktet kunne ikke oprettes i systemet", ConfirmationTool.danger);
        return REDIRECT + PRODUCTS;
    }

    //Asbjørn
    @GetMapping("/createtreatment")
    public String createTreatment(Model model, HttpSession session) {
        log.info("get createtreatment action started..." + SessionLog.sessionId(session));

        if(!userAuthenticator.userIsAdmin(session)){
            log.info(SessionLog.NOTADMIN + SessionLog.sessionId(session));

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
        log.info("post createtreatment action started..." + SessionLog.sessionId(session));

        if(!userAuthenticator.userIsAdmin(session)){
            log.info(SessionLog.NOTADMIN + SessionLog.sessionId(session));

            return REDIRECT;
        }

        if (productServiceImpl.createTreatment(treatment)) {
            log.info("created treatment: " + treatment.getProductName() + "..." + SessionLog.sessionId(session));

            confirmationTool.confirmation(treatment.getProductName() + " er blevet oprettet som behandling i systemet", ConfirmationTool.success);
            return REDIRECT + TREATMENTS;
        }
        log.info("could not create treatment: " + treatment.getProductName() + "..." + SessionLog.sessionId(session));

        confirmationTool.confirmation("Behandlingen kunne ikke oprettes i systemet", ConfirmationTool.danger);
        return REDIRECT + TREATMENTS;
    }

    //Asbjørn
    @GetMapping ("/treatments")
    public String displayTreatments (Model model, HttpSession session) {
        log.info("get treatments action started..." + SessionLog.sessionId(session));

        UserAccessController.userExists(model, session);

        model.addAttribute("loginToken", new LoginToken());
        model.addAttribute("treatments", productServiceImpl.createTreatmentArrayList());
        model.addAttribute("items", productServiceImpl.createItemArrayList());
        model.addAttribute("showTreatments", true);
        model.addAttribute("showProducts", false);
        confirmationTool.showConfirmation(model);
        return DISPLAYPRODUCTS;
    }

    //Asbjørn
    @GetMapping ("/products")
    public String displayProducts (Model model, HttpSession session) {
        log.info("get products action started..." + SessionLog.sessionId(session));

        UserAccessController.userExists(model, session);

        model.addAttribute("loginToken", new LoginToken());
        model.addAttribute("treatments", productServiceImpl.createTreatmentArrayList());
        model.addAttribute("items", productServiceImpl.createItemArrayList());
        model.addAttribute("showProducts", true);
        model.addAttribute("showTreatments", false);
        confirmationTool.showConfirmation(model);
        return DISPLAYPRODUCTS;
    }

    //Mike
    @GetMapping("/editproduct")
    public String editProduct(Model model, HttpSession session) {
        log.info("get editproduct action started..." + SessionLog.sessionId(session));

        if(!userAuthenticator.userIsAdmin(session)){
            log.info(SessionLog.NOTADMIN + SessionLog.sessionId(session));

            return REDIRECT;
        }
        User user = (User)session.getAttribute("user");
        model.addAttribute("user", user);
        return EDITPRODUCT;
    }

    //Asbjørn
    @GetMapping ("/edittreatment/{id}")
    public String editTreatment (@PathVariable("id") int id, Model model, HttpSession session) {
        log.info("get edittreatment action started..." + SessionLog.sessionId(session));

        if(!userAuthenticator.userIsAdmin(session)){
            log.info(SessionLog.NOTADMIN + SessionLog.sessionId(session));

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
        log.info("post edittreatment action started..." + SessionLog.sessionId(session));

        if(!userAuthenticator.userIsAdmin(session)){
            log.info(SessionLog.NOTADMIN + SessionLog.sessionId(session));

            return REDIRECT;
        }

        if (productServiceImpl.editTreatment(treatment)) {
            log.info("edited treatment: " + treatment.getProductName() + "..." + SessionLog.sessionId(session));

            confirmationTool.confirmation("Information på behandlingen: " + treatment.getProductName() + " er blevet ændret i systemet", ConfirmationTool.success);
            return REDIRECT + "treatments";
        }
        log.info("could not edit treatment: " + treatment.getProductName() + "..." + SessionLog.sessionId(session));

        confirmationTool.confirmation("Behandlingens information kunne IKKE ændres i systemet", ConfirmationTool.danger);
        return REDIRECT + "treatments";
    }

    //Asbjørn
    @GetMapping ("/edititem/{id}")
    public String editItem (@PathVariable ("id") int id, Model model, HttpSession session) {
        log.info("get edititem action started..." + SessionLog.sessionId(session));

        if(!userAuthenticator.userIsAdmin(session)){
            log.info(SessionLog.NOTADMIN + SessionLog.sessionId(session));

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
        log.info("post edititem action started..." + SessionLog.sessionId(session));

        if(!userAuthenticator.userIsAdmin(session)){
            log.info(SessionLog.NOTADMIN + SessionLog.sessionId(session));

            return REDIRECT;
        }

        if (productServiceImpl.editItem(item)) {
            log.info("edited item: " + item.getProductName() + "..." + SessionLog.sessionId(session));

            confirmationTool.confirmation("Information på " + item.getProductName() + " er blevet ændret i systemet", ConfirmationTool.success);
            return REDIRECT + "products";
        }
        log.info("could not edit item: " + item.getProductName() + "..." + SessionLog.sessionId(session));

        confirmationTool.confirmation("Produktets information kunne IKKE ændres i systemet", ConfirmationTool.danger);
        return REDIRECT + "products";
    }

}
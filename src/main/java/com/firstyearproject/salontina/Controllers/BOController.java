package com.firstyearproject.salontina.Controllers;

import com.firstyearproject.salontina.Handlers.SMSHandler;
import com.firstyearproject.salontina.Models.Newsletter;
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

    @Autowired
    SMSHandler sMSHandler;

    private String NEWSLETTER = "newsletter";
    private String REDIRECTNEWSLETTER = "redirect:/" + NEWSLETTER;

    //Luca
    @GetMapping("newsletter")
    public String newsletter(Model model, HttpSession session){
        log.info("get newsletter action started...");

        model.addAttribute("newsletter", new Newsletter());
        return NEWSLETTER;
    }

    //Luca
    @PostMapping("sendnewsletter")
    public String sendNewsletter(Model model, HttpSession session, @ModelAttribute Newsletter newsletter){
        log.info("post newsletter action started...");

        if(sMSHandler.sendNewsletter(newsletter.getText())){
            log.info("newsletter was successfully sent...");
        }
        return REDIRECTNEWSLETTER;
    }

    //Luca
    @PostMapping("sendtestnewsletter")
    public String sendTestNewsletter(Model model, HttpSession session,
                                     @ModelAttribute Newsletter newsletter){
        log.info("post newsletter action started...");

        if(sMSHandler.sendNewsletterTest(newsletter.getTestNumber(), newsletter.getText())){
            log.info("newsletter was successfully sent...");
        }
        return REDIRECTNEWSLETTER;
    }

}

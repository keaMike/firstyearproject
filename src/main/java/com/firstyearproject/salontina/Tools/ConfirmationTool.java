package com.firstyearproject.salontina.Tools;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class ConfirmationTool {

    private boolean showConfirmation = false;
    private String confirmationText = "";
    private String alert = "";
    public static String danger = "alert alert-danger";
    public static String success = "alert alert-success";

    /**
     * Luca
     * Used in Java Methods/mappings
     */
    public void confirmation(String text, String alert){
        showConfirmation = true;
        confirmationText = text;
        this.alert = alert;
    }

    /**
     * Luca
     * Sends data to HTML-modals
     */
    public void showConfirmation(Model model){
        model.addAttribute("showconfirmation", showConfirmation);
        model.addAttribute("confirmationtext", confirmationText);
        model.addAttribute("alert", alert);
        showConfirmation = false;
    }

}

package com.firstyearproject.salontina.Handlers;

import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SMSHandler {

    public boolean sendReminder(Date date){
        return false;
    }

    public boolean sendNewsletter(String text){
        return false;
    }

    public boolean sendNewsletterTest(int phonenumber, String text){
        return false;
    }

    public void sendSMS(int phonenumber, String text){

    }

}

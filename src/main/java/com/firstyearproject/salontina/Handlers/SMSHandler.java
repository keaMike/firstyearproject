package com.firstyearproject.salontina.Handlers;

import com.firstyearproject.salontina.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SMSHandler {

    @Autowired
    UserRepo userRepo;

    @Autowired
    SMSConnector smsConnector;

    public boolean sendReminder(Date date){
        return false;
    }

    public boolean sendNewsletter(String text){
        List<String> phonenumbers = userRepo.getNewsletterList();

        for(String s : phonenumbers){
            if(!s.substring(0,3).equals("+45")){
                s = "+45" + s;
            }
        }

        for(int i = 0; i < phonenumbers.size(); i++){
            if(!phonenumbers.get(i).substring(0,3).equals("+45")){
                String phonenumber = phonenumbers.get(i);

                phonenumber = "+45" + phonenumber;

                phonenumbers.remove(i);
                phonenumbers.add(phonenumber);
            }
        }

        for(String s : phonenumbers){
            sendSMS(s, text);
        }
        return true;
    }

    public boolean sendNewsletterTest(String phonenumber, String text){
        sendSMS(phonenumber, text);
        return true;
    }

    private void sendSMS(String phonenumber, String text){
        smsConnector.sendSMS(phonenumber, text);
    }

}

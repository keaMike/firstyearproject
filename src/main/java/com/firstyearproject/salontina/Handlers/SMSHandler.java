package com.firstyearproject.salontina.Handlers;

import com.firstyearproject.salontina.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//Luca
@Service
public class SMSHandler {

    @Autowired
    UserRepo userRepo;

    @Autowired
    SMSConnector smsConnector;

    public boolean sendReminder(Date date){
        return false;
    }

    //Luca
    public boolean sendNewsletter(String text){
        List<String> phonenumbers = getTestNewsletterList(); //userRepo.getNewsletterList();

        for(String s : phonenumbers){
            if(verifyNumber(s) == null){
                break;
            }
            sendSMS(s, text);
        }
        return true;
    }

    //Luca
    public boolean sendNewsletterTest(String phonenumber, String text){
        sendSMS(phonenumber, text);
        return true;
    }

    //Luca
    private void sendSMS(String phonenumber, String text){
        smsConnector.sendSMS(phonenumber, text);
    }

    //Luca
    private String verifyNumber(String number){
        if(!number.substring(0,3).equals("+45")){
            number = "+45" + number;
        }
        if(number.length() != 11){
            return null;
        }
        return number;
    }

    //Luca
    private List<String> getTestNewsletterList(){
        List<String> testList = new ArrayList<>();

        testList.add("22210051");
        testList.add("42241480");
        testList.add("22323386");
        testList.add("40266249");

        return testList;
    }

}

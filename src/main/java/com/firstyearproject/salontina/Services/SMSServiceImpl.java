package com.firstyearproject.salontina.Services;

import com.firstyearproject.salontina.Repositories.UserRepoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//Luca
@Service
public class SMSServiceImpl implements SMSService{

    @Autowired
    UserRepoImpl userRepoImpl;

    @Autowired
    SMSConnector smsConnector;

    //Luca
    public boolean sendReminder(){
        List<String> phonenumbers = userRepoImpl.getReminderList();

        String reminderText = "Hej, du har en tid hos Salon Tina i morgen.";

        sendSMSToList(phonenumbers, reminderText);
        return true;
    }

    //Luca
    //Method gets a list of phonenumbers and sends text to them
    public boolean sendNewsletter(String text){
        List<String> phonenumbers = getTestNewsletterList(); //userRepo.getNewsletterList();

        sendSMSToList(phonenumbers, text);
        return true;
    }

    //Luca
    public boolean sendNewsletterTest(String phonenumber, String text){
        sendSMS(verifyNumber(phonenumber), text);
        return true;
    }

    //Luca
    public void sendSMSToList(List<String> numberList, String text){
        for(String s : numberList){
            sendSMS(verifyNumber(s), text);
        }
    }

    //Luca
    //Method is only called internally in the class
    private void sendSMS(String phonenumber, String text){
        smsConnector.sendSMS(phonenumber, text);
    }

    //Luca
    //Method validates (or fixes) that number starts with +45 and is the correct length
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
    //Method only relevant in development, as SMS-API only sends to 'verified' numbers,
    //when using trial account.
    private List<String> getTestNewsletterList(){
        List<String> testList = new ArrayList<>();

        testList.add("22210051");
        testList.add("42241480");
        testList.add("22323386");
        testList.add("40266249");

        return testList;
    }

}

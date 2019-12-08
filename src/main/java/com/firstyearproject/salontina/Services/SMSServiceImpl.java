package com.firstyearproject.salontina.Services;

import com.firstyearproject.salontina.Models.Reminder;
import com.firstyearproject.salontina.Repositories.BookingRepoImpl;
import com.firstyearproject.salontina.Repositories.UserRepoImpl;
import com.twilio.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//Luca
@Service
public class SMSServiceImpl implements SMSService{

    @Autowired
    UserRepoImpl userRepoImpl;

    @Autowired
    SMSConnector smsConnector;

    @Autowired
    BookingRepoImpl bookingRepo;

    //Luca
    @Override
    public boolean sendReminder(){
        try {
            List<Reminder> reminderList = userRepoImpl.getReminderList();

            for(Reminder r : reminderList){
                String reminderText = "Hej " + r.getReminderUsername() + " du har en tid d. " + r.getReminderDate() + " kl. " + r.getReminderTime() + " hos Salon Tina.";
                sendSMS(verifyNumber(r.getReminderPhonenumber()), reminderText);
            }

            bookingRepo.saveReminder();

            return true;
        } catch (com.twilio.exception.ApiException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Luca
    //Method gets a list of phonenumbers and sends text to them
    @Override
    public boolean sendNewsletter(String text){
        try {
            List<String> phonenumbers = getTestNewsletterList(); //userRepo.getNewsletterList();

            sendSMSToList(phonenumbers, text);
            return true;
        } catch (com.twilio.exception.ApiException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Luca
    @Override
    public boolean sendNewsletterTest(String phonenumber, String text){
        try {
            sendSMS(verifyNumber(phonenumber), text);
            return true;
        } catch (com.twilio.exception.ApiException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Luca
    @Override
    public void sendSMSToList(List<String> numberList, String text) throws ApiException{
        for(String s : numberList){
            sendSMS(verifyNumber(s), text);
        }
    }

    //Luca
    //Method is only called internally in the class
    private void sendSMS(String phonenumber, String text) throws ApiException{
        smsConnector.sendSMS(phonenumber, text);
    }

    //Luca
    //Method validates (or fixes) that number starts with +45 and is the correct length
    private String verifyNumber(String number) throws ApiException {
        if(number.length() > 2 && !number.substring(0,3).equals("+45")){
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

package com.firstyearproject.salontina.Services;

import com.firstyearproject.salontina.Models.Reminder;
import com.firstyearproject.salontina.Repositories.UserRepoImpl;
import com.twilio.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

//Luca
@Service
public class SMSServiceImpl implements SMSService{

    //Asbjørn
    //Used in method for scheduling smsReminders. Creates a thread
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserRepoImpl userRepoImpl;

    @Autowired
    SMSConnector smsConnector;

    //Luca
    public boolean sendReminder(){
        try {
            List<Reminder> reminderList = userRepoImpl.getReminderList();

            for(Reminder r : reminderList){
                String reminderText = "Hej " + r.getReminderUsername() + " du har en tid d. " + r.getReminderDate() + " kl. " + r.getReminderTime() + " hos Salon Tina.";
                sendSMS(verifyNumber(r.getReminderPhonenumber()), reminderText);
            }

            return true;
        } catch (com.twilio.exception.ApiException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Luca
    //Method gets a list of phonenumbers and sends text to them
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

    //Asbjørn
    //This method creates a thread and schedules a
    public void initiateAutoReminder(String manualOverride) {
        log.info("AutoReminder initiated");
        final Runnable autoReminder = new Runnable() {
            @Override
            public void run() {
                sendNewsletter("Autotest - daily"); //This method is for testing/demonstration
                //sendReminder(); //This method is the actual purpose of autoReminder
            }
        };
        final ScheduledFuture<?> autoReminderHandler = scheduler.scheduleAtFixedRate(autoReminder, 6, 24, TimeUnit.HOURS);
        //The first int is the first delay after initialization. The second int is the delay between each method.
        //TimeUnit can be configured to the desired unit of measure

        scheduler.schedule(new Runnable() {
            @Override
            public void run() {autoReminderHandler.cancel(true); }
        }, 14, TimeUnit.DAYS);
        //This method is used to limit how long the autoReminder runs
        //AutoReminder can be reinitialized be opening the FrontOffice page

        if(manualOverride.equalsIgnoreCase("cancel")) {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {autoReminderHandler.cancel(true); }
            }, 1, TimeUnit.SECONDS);
        }
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

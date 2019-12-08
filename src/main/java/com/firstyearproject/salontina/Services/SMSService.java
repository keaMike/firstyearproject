package com.firstyearproject.salontina.Services;

import java.util.List;

public interface SMSService {

    boolean sendReminder();
    boolean sendNewsletter(String text);
    boolean sendNewsletterTest(String phonenumber, String text);
    void sendSMSToList(List<String> numberList, String text);
}

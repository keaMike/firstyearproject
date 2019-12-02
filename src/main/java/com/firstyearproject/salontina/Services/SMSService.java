package com.firstyearproject.salontina.Services;

import java.util.Date;

public interface SMSService {

    boolean sendReminder(Date date);
    boolean sendNewsletter(String text);
    boolean sendNewsletterTest(String phonenumber, String text);
}

package com.firstyearproject.salontina.Services;

import java.util.Date;

public interface SMSService {

    boolean sendReminder();
    boolean sendNewsletter(String text);
    boolean sendNewsletterTest(String phonenumber, String text);
}

package com.firstyearproject.salontina.Tools;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Luca
 */
@Component
public class SMSConnector {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static SMSConnector singleInstance = null;

    static SMSConnector getInstance(){
        if(singleInstance == null){
            singleInstance = new SMSConnector();
        }
        return singleInstance;
    }

    @Value("${sms.account}")
    private String ACCOUNT_SID;

    @Value("${sms.auth}")
    private String AUTH_TOKEN;

    @Value("${sms.fromnumber}")
    private String FROMNUMBER;

    public void sendSMS(String number, String text) throws ApiException {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message
                .creator(new PhoneNumber(number), // to
                        new PhoneNumber(FROMNUMBER), // from
                        text)
                .create();

        log.info("sms: '" + text + "', to: '" + number + "'");
        log.info("sms id: " + message.getSid());
    }

}

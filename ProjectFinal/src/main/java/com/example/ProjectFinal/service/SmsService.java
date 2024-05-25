package com.example.ProjectFinal.service;

import com.example.ProjectFinal.PhoneNumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class SmsService {

    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    @Value("${twilio.phoneNumber}")
    private String twilioPhoneNumber;

    public void sendSms(String toPhoneNumber, String message) {
        String formattedPhoneNumber = PhoneNumberUtils.formatPhoneNumber(toPhoneNumber);

        if (formattedPhoneNumber != null) {
            Twilio.init(accountSid, authToken);
            Message twilioMessage = Message.creator(
                    new PhoneNumber(formattedPhoneNumber),
                    new PhoneNumber(twilioPhoneNumber),
                    message
            ).create();

            System.out.println("SMS envoyé avec SID : " + twilioMessage.getSid());
        } else {
            System.err.println("Impossible d'envoyer le SMS. Numéro de téléphone invalide.");
        }
    }
}

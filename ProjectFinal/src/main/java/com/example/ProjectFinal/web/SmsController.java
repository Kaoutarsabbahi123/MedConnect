package com.example.ProjectFinal.web;

import com.example.ProjectFinal.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SmsController {

    @Autowired
    private SmsService smsService;

    @PostMapping("/envoyer-sms")
    public void envoyerSms(@RequestBody String numéro) {
        String message = "Bonjour! Ceci est un SMS envoyé depuis Spring Boot.";
        smsService.sendSms(numéro, message);
    }
}


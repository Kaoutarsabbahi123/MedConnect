package com.example.ProjectFinal.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String loginagent(Model model) {
        return "login";
    }
    @GetMapping("/SendEmail")
    public String SendEmail() {
        return "email";
    }
    @GetMapping("/verification")
    public String Sendcode() {
        return "verification";
    }
    @GetMapping("/RecupereMdp")
    public String RecupereMdp() {
        return "motdepasse";
    }
    @GetMapping("/sms")
    public String sms(){return "sms";}

}

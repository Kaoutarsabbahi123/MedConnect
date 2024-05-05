package com.example.ProjectFinal.web;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class VerificationCodeController {

    @PostMapping("/api/verifierCode")
    public ResponseEntity<String> verifyCode(
            @RequestParam("code1") String code1,
            @RequestParam("code2") String code2,
            @RequestParam("code3") String code3,
            @RequestParam("code4") String code4,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        String codeSession = (String) session.getAttribute("verificationCode");
        String codeSaisi = code1 + code2 + code3 + code4;
        Integer remainingAttempts = (Integer) session.getAttribute("remainingAttempts");

        if (codeSession != null && codeSession.equals(codeSaisi)) {
            return ResponseEntity.ok("Code correct. Redirection en cours vers la page de réinitialisation de mot de passe...");
        } else {
            if (remainingAttempts == null) {
                remainingAttempts = 3;
            }
            remainingAttempts--;

            if (remainingAttempts > 0) {
                session.setAttribute("remainingAttempts", remainingAttempts);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vous avez encore " + remainingAttempts + " tentative(s) restante(s).");
            } else {
                session.removeAttribute("remainingAttempts");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tentatives épuisées. Veuillez contacter le support.");
            }

        }
    }
}

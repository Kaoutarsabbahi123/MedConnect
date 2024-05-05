package com.example.ProjectFinal.web;

import com.example.ProjectFinal.UserLoginRequest;
import com.example.ProjectFinal.entities.User;
import com.example.ProjectFinal.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginRequest loginRequest, HttpSession session) {
        String login = loginRequest.getLogin();
        String password = loginRequest.getPassword();

        // Récupérer la carte des tentatives échouées depuis la session
        Map<String, Integer> failedAttemptsMap = (Map<String, Integer>) session.getAttribute("failedAttemptsMap");
        if (failedAttemptsMap == null) {
            failedAttemptsMap = new HashMap<>();
            session.setAttribute("failedAttemptsMap", failedAttemptsMap);
        }

        // Récupérer le nombre de tentatives échouées pour cet utilisateur depuis la carte
        Integer failedAttempts = failedAttemptsMap.getOrDefault(login, 0);

        // Vérifier si le nombre de tentatives dépasse le seuil
        if (failedAttempts >= 3) {
            session.removeAttribute("failedAttemptsMap");
            // Bloquer la connexion si le seuil est atteint
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Trop de tentatives de connexion. Veuillez réessayer plus tard.");
        }

        // Recherche de l'utilisateur dans la base de données
        User user = userRepository.findByLogin(login);

        // Vérifie si l'utilisateur existe et si le mot de passe est correct
        if (user != null && user.getMot_passe().equals(password)) {
            // Réinitialiser le nombre de tentatives échouées si la connexion réussit
            failedAttemptsMap.remove(login);

            // Stocke l'utilisateur dans la session
            session.setAttribute("user", user);
            return ResponseEntity.ok("Login successful");
        } else {
            // Incrémenter le nombre de tentatives échouées pour cet utilisateur
            failedAttemptsMap.put(login, failedAttempts + 1);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
        }
    }


}

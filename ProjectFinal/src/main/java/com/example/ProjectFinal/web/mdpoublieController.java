package com.example.ProjectFinal.web;

import com.example.ProjectFinal.repositories.UserRepository;
import com.example.ProjectFinal.entities.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class mdpoublieController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/reinitialiser_mdp")
    public ResponseEntity<String> reinitialiserMotDePasse(
            @RequestParam("nv_mdp") String nv_mdp,
            @RequestParam("confirmpassword") String confirmpass,
            HttpSession session) {

        String resetEmail = (String) session.getAttribute("email");
        User user = userRepository.findByLogin(resetEmail);

        if (!nv_mdp.equals(confirmpass)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Les mots de passe ne correspondent pas");
        }
//        user.setMot_passe(nv_mdp);
        userRepository.save(user);

        return ResponseEntity.ok().body("Mot de passe réinitialisé avec succès");
    }
}




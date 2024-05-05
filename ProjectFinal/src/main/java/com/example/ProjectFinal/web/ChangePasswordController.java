package com.example.ProjectFinal.web;

import com.example.ProjectFinal.entities.User;
import com.example.ProjectFinal.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class ChangePasswordController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestParam("currentPassword") String currentPassword,
                                                 @RequestParam("newPassword") String newPassword,
                                                 HttpSession session) {
        User user = (User) session.getAttribute("user");
        User currentuser=userRepository.findByLogin(user.getLogin());
        if (currentuser == null) {
            return ResponseEntity.badRequest().body("Utilisateur non trouvé !");
        }

        // Vérifier si le mot de passe actuel correspond au mot de passe de l'utilisateur
        if (!currentPassword.equals(currentuser.getMot_passe())) {
            return ResponseEntity.badRequest().body("Mot de passe actuel incorrect !");
        }

        // Mettre à jour le mot de passe de l'utilisateur
        currentuser.setMot_passe(newPassword);
        userRepository.save(currentuser);
        return ResponseEntity.ok("Mot de passe modifié avec succès !");
    }
}

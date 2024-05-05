package com.example.ProjectFinal.web;


import com.example.ProjectFinal.entities.User;
import com.example.ProjectFinal.repositories.UserRepository;
import com.example.ProjectFinal.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProfileController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String userProfile(Model model, HttpSession session) {
        // Récupérer l'utilisateur à partir de la session
        User user = (User) session.getAttribute("user");
        // Récupérer l'utilisateur depuis la base de données pour obtenir les données actualisées
        User currentUser = userRepository.findByLogin(user.getLogin());
        model.addAttribute("user", currentUser);
        return "profile";
    }
    @PostMapping("/update-profile")
    public String updateProfile(@ModelAttribute("user") User user, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        currentUser.setCIN(user.getCIN());
        currentUser.setNom(user.getNom());
        currentUser.setPrenom(user.getPrenom());
        currentUser.setSexe(user.getSexe());
        currentUser.setDat_naiss(user.getDat_naiss());
        currentUser.setAddress(user.getAddress());
        currentUser.setTel(user.getTel());
        userService.update(currentUser);
        return "redirect:/profile?Success=true";
    }


}
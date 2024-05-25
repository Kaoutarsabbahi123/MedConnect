package com.example.ProjectFinal.web;

import com.example.ProjectFinal.entities.Specialité;
import com.example.ProjectFinal.repositories.SpecialiteRepository;
import com.example.ProjectFinal.service.Docteurservice;
import com.example.ProjectFinal.service.PatientService;
import com.example.ProjectFinal.service.RendezVousService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {
    @Autowired
    private Docteurservice docteurService;
    @Autowired
    private RendezVousService rendezVousService;

    @Autowired
    private PatientService patientService;
    @Autowired
    private SpecialiteRepository specialiteRepository;

    @GetMapping("/Dashboard")
    public String dashboard(Model model) {
        // Récupérer le nombre réel de médecins depuis le service
        int nombreMedecins = docteurService.getNombreMedecins();
        // Ajouter le nombre de médecins au modèle
        model.addAttribute("nombreMedecins", nombreMedecins);
        int nombreRendezVous = rendezVousService.getNombreRendezVous();
        // Ajouter le nombre de rendez-vous au modèle
        model.addAttribute("nombreRendezVous", nombreRendezVous);

        // Récupérer le nombre réel de patients depuis le service
        int nombrePatients = patientService.getNombrePatients();
        // Ajouter le nombre de patients au modèle
        model.addAttribute("nombrePatients", nombrePatients);
        return "dashboard"; // Retourne le nom de la vue Thymeleaf
    }

}

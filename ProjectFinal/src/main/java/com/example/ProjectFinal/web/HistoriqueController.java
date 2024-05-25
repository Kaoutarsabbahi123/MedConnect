package com.example.ProjectFinal.web;

import com.example.ProjectFinal.entities.Docteur;
import com.example.ProjectFinal.entities.rendez_Vous;
import com.example.ProjectFinal.repositories.DocteurRepository;
import com.example.ProjectFinal.repositories.RendezVousRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
public class HistoriqueController {

    @Autowired
    private RendezVousRepository rendezVousRepository;
    @Autowired
    private DocteurRepository docteurRepository;

    @GetMapping("/Historique")
    public String listRendezVous(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(name = "docteur", required = false) Integer doctorId,
                                 @RequestParam(name = "date_RDV", required = false) String date,
                                 Model model) {

        int pageSize = 10;
        Page<rendez_Vous> rendezVousPage;

        if (doctorId != null && date != null && !date.isEmpty()) {
            // Convertir la chaîne de date en objet java.util.Date
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            try {
                java.util.Date parsedDate = formatter.parse(date);
                Date sqlDate = new Date(parsedDate.getTime());

                // Récupérer les rendez-vous pour le docteur et la date spécifiés
                rendezVousPage = rendezVousRepository.findByDocteurIdAndDateRDVAndStatutNot(doctorId, sqlDate, PageRequest.of(page, pageSize));
            } catch (ParseException e) {
                // Gérer l'erreur de parsing de la date
                rendezVousPage = rendezVousRepository.findByStatutRDVNot("en attente", PageRequest.of(page, pageSize));
                model.addAttribute("error", "Erreur lors du parsing de la date.");
            }
        } else {
            // Récupérer tous les rendez-vous sans statut "en attente" si les paramètres ne sont pas fournis
            rendezVousPage = rendezVousRepository.findByStatutRDVNot("en attente", PageRequest.of(page, pageSize));
        }

        List<Docteur> doctors = docteurRepository.findAll();
        model.addAttribute("doctors", doctors);

        // Envoyer les données à la vue Thymeleaf
        model.addAttribute("rdvs", rendezVousPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", rendezVousPage.getTotalPages());

        return "Historique";
    }


}

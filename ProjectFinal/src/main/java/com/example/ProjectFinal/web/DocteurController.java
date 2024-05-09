
package com.example.ProjectFinal.web;

import com.example.ProjectFinal.entities.Horaire;
import com.example.ProjectFinal.entities.Specialité;
import com.example.ProjectFinal.repositories.HoraireRepository;
import com.example.ProjectFinal.repositories.RendezVousRepository;
import com.example.ProjectFinal.repositories.SpecialiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.ProjectFinal.repositories.DocteurRepository;
import com.example.ProjectFinal.entities.Docteur;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
public class DocteurController {

    public final DocteurRepository docteurRepository;
    public final SpecialiteRepository specialiteRepository;

    public final HoraireRepository horaireRepository;
    public final RendezVousRepository rendezVousRepository;
    @Autowired
    public DocteurController(DocteurRepository docteurRepository, SpecialiteRepository specialiteRepository, HoraireRepository horaireRepository, RendezVousRepository rendezVousRepository) {
        this.docteurRepository = docteurRepository;
        this.specialiteRepository = specialiteRepository;
        this.horaireRepository = horaireRepository;
        this.rendezVousRepository = rendezVousRepository;
    }

    @GetMapping("/rendezvous")
    public String getAllSpecialites(Model model) {
        List<Specialité> specialites = specialiteRepository.findAll(); // Remplacez specialiteRepository par votre repository pour les spécialités
        model.addAttribute("specialites", specialites);
        return "rendezvous";
    }
    @GetMapping("/Calendar")
    public String showDoctors(Model model) {
        List<Docteur> doctors = docteurRepository.findAll();
        model.addAttribute("doctors", doctors);
        return "Calendar"; // Ceci correspond au nom de votre fichier HTML Thymeleaf (doctors.html)
    }
    @GetMapping("/specialite/{id}/docteurs")
    @ResponseBody
    public ResponseEntity<List<Docteur>> getDocteursBySpecialiteId(@PathVariable("id") int id) {
        Optional<Specialité> optionalSpecialite = specialiteRepository.findById(id);
        if (optionalSpecialite.isPresent()) {
            Specialité specialite = optionalSpecialite.get();
            List<Docteur> docteurs = specialite.getDocteurs();
            List<Docteur> docteurDtos = new ArrayList<>();
            for (Docteur docteur : docteurs) {
                Docteur docteurDto = new Docteur();
                docteurDto.setId_docteur(docteur.getId_docteur());
                docteurDto.setNom(docteur.getNom());
                docteurDtos.add(docteurDto);
            }
            return ResponseEntity.ok(docteurDtos);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/docteur/{id}/dates-dispo")
    @ResponseBody
    public ResponseEntity<List<Date>> getDatesDispoByDocteurId(@PathVariable("id") int id) {
        Optional<Docteur> optionalDocteur = docteurRepository.findById(id);
        if (optionalDocteur.isPresent()) {
            Docteur docteur = optionalDocteur.get();
            List<Horaire> horaires = docteur.getHoraires();
            List<Date> datesDispo = new ArrayList<>();
            for (Horaire horaire : horaires) {
                datesDispo.add(horaire.getDate_dispo());
            }
            return ResponseEntity.ok(datesDispo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/api/consultation-hours")
    public ResponseEntity<List<String>> getAvailableConsultationHours(
            @RequestParam("date") String selectedDate,
            @RequestParam("doctorId") int doctorId) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date parsedDate = formatter.parse(selectedDate);
        Date sqlDate = new Date(parsedDate.getTime());
        Optional<Docteur> optionalDocteur = docteurRepository.findById(doctorId);
        if (optionalDocteur.isPresent()) {
            Docteur docteur = optionalDocteur.get();
            List<String> availableHours = new ArrayList<>();

            for (Horaire horaire : docteur.getHoraires()) {
                // Vérifier si l'horaire correspond à la date sélectionnée
                if (horaire.getDate_dispo().equals(sqlDate)) {
                    Time startTime = horaire.getHeure_debut();
                    Time endTime = horaire.getHeure_fin();

                    while (startTime.before(endTime)) {
                        // Vérifier si l'heure est disponible (moins de 5 rendez-vous à cette heure)
                        if (isConsultationHourAvailable(docteur, sqlDate, startTime)) {
                            availableHours.add(startTime.toString());
                        }
                        startTime = addOneHour(startTime);
                    }
                }
            }
            return ResponseEntity.ok(availableHours);
        }

        return ResponseEntity.ok(Collections.emptyList());
    }

    // Méthode pour vérifier si une heure de consultation est disponible
    private boolean isConsultationHourAvailable(Docteur doctorId, Date selectedDate, Time consultationTime) {
        long countAppointments = rendezVousRepository.countByDocteurAndDateRDVAndHeureConsultation(doctorId, selectedDate, consultationTime);

        return countAppointments < 5; // Vérifie si moins de 5 rendez-vous sont planifiés à cette heure
    }
    private Time addOneHour(Time time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        return new Time(calendar.getTimeInMillis());
    }
}





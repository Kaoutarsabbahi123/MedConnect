package com.example.ProjectFinal.web;

import com.example.ProjectFinal.entities.Docteur;
import com.example.ProjectFinal.entities.patientNonAuthetifie;
import com.example.ProjectFinal.entities.rendez_Vous;
import com.example.ProjectFinal.repositories.DocteurRepository;
import com.example.ProjectFinal.repositories.PatientNonAuthetifieRepository;
import com.example.ProjectFinal.repositories.RendezVousRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@RestController
@RequestMapping("/api/rendezvous")
public class RendezVousController {

    @Autowired
    private RendezVousRepository rendezVousRepository;

    @Autowired
    private PatientNonAuthetifieRepository patientNonAuthetifieRepository;

    @Autowired
    private DocteurRepository docteurRepository;

    @PostMapping("/save")
    public ResponseEntity<String> saveRendezVous(
            @RequestParam String email,
            @RequestParam String cin,
            @RequestParam String nom,
            @RequestParam String prenom,
            @RequestParam String sexe,
            @RequestParam String adresse,
            @RequestParam String telephone,
            @RequestParam String dateNaissance,
            @RequestParam String docteur,
            @RequestParam String date_RDV ,
            @RequestParam String heure_consultation) {

        rendez_Vous rendezVous = new rendez_Vous();
        patientNonAuthetifie patient = new patientNonAuthetifie();
        Optional<Docteur> optionalDocteur = docteurRepository.findById(Integer.valueOf(docteur));

        if (optionalDocteur.isPresent()) {
            Docteur doctor = optionalDocteur.get();

            // Traitement pour sauvegarder le rendez-vous
            patient.setNom(nom);
            patient.setPrenom(prenom);
            patient.setCIN(cin);
            patient.setEmail(email);

            try {
                LocalDate parsedDateNaiss = LocalDate.parse(dateNaissance, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                patient.setDat_naiss(Date.valueOf(parsedDateNaiss));
            } catch (DateTimeParseException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de la conversion de la date de naissance.");
            }

            patient.setSexe(sexe);
            patient.setAddress(adresse);
            patient.setTel(telephone);
            patientNonAuthetifieRepository.save(patient);

            try {
                LocalDate parsedDateRDV = LocalDate.parse(date_RDV, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                rendezVous.setDate_RDV(Date.valueOf(parsedDateRDV));
                rendezVous.setHeure_consultation(Time.valueOf(heure_consultation));
                rendezVous.setPatientNA(patient);
                rendezVous.setDocteur(doctor);
                rendezVousRepository.save(rendezVous);

                return ResponseEntity.ok("Rendez-vous enregistré avec succès !");
            } catch (DateTimeParseException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de la conversion de la date du rendez-vous.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Docteur non trouvé.");
        }
    }
}

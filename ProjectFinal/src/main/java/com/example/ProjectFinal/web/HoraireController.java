package com.example.ProjectFinal.web;

import com.example.ProjectFinal.ResourceNotFoundException;
import com.example.ProjectFinal.entities.Docteur;
import com.example.ProjectFinal.entities.Horaire;
import com.example.ProjectFinal.repositories.DocteurRepository;
import com.example.ProjectFinal.repositories.HoraireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

@RestController
public class HoraireController {

    @Autowired
    private HoraireRepository horaireRepository;
    private DocteurRepository docteurRepository;

    @PostMapping("/api/horaires")
    public ResponseEntity<Horaire> saveHoraire(@RequestParam int doctorId,
                                               @RequestParam String startTime,
                                               @RequestParam String endTime,
                                               @RequestParam String date) {
        try {
            // Créer un nouvel objet Horaire avec les données reçues
            Horaire horaire = new Horaire();
            Optional<Docteur> docteur=docteurRepository.findById(doctorId);
            Docteur doctor=docteur.get();
            horaire.setDocteur(doctor);
            horaire.setHeure_debut(Time.valueOf(startTime));
            horaire.setHeure_fin(Time.valueOf(endTime));
            horaire.setDate_dispo(Date.valueOf(date));

            System.out.println(horaire);
            Horaire savedHoraire = horaireRepository.save(horaire);

            return new ResponseEntity<>(savedHoraire, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to save horaire", e);
        }
    }
}


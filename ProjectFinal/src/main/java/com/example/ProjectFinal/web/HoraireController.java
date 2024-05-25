package com.example.ProjectFinal.web;

import com.example.ProjectFinal.entities.Docteur;
import com.example.ProjectFinal.entities.Horaire;
import com.example.ProjectFinal.repositories.DocteurRepository;
import com.example.ProjectFinal.repositories.HoraireRepository;
import com.sun.jdi.request.EventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Optional;

@RestController
public class HoraireController {

    @Autowired
    private HoraireRepository horaireRepository;

    @Autowired
    private DocteurRepository docteurRepository;

    // Classe DTO pour représenter les détails de l'horaire
        public static class HoraireDTO {
            private Long eventId; // Identifiant unique de l'horaire
            private String doctorName;
            private String startTime;
            private String endTime;
            private Date date;
            private int DoctorId;

            public HoraireDTO(Long eventId, String doctorName, String startTime, String endTime, Date date,int DoctorId) {
                this.eventId = eventId;
                this.doctorName = doctorName;
                this.startTime = startTime;
                this.endTime = endTime;
                this.date = date;
                this.DoctorId=DoctorId;
            }

            // Getters et Setters
            public Long getEventId() {
                return eventId;
            }

            public void setEventId(Long eventId) {
                this.eventId = eventId;
            }
        // Getters et Setters
        public String getDoctorName() {
            return doctorName;
        }

        public void setDoctorName(String doctorName) {
            this.doctorName = doctorName;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public int getDoctorId() {
            return DoctorId;
        }

        public void setDoctorId(int doctorId) {
            DoctorId = doctorId;
        }
    }

    @PostMapping("/api/horaires")
    public ResponseEntity<HoraireDTO> saveHoraire(@RequestParam("doctorId") int doctorId,
                                                  @RequestParam("startTime") String startTime,
                                                  @RequestParam("endTime") String endTime,
                                                  @RequestParam("date") String date) {
        try {
            // Convertir les valeurs de temps et date
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            java.util.Date parsedDate = sdf.parse(startTime);
            Time startTimeValue = new Time(parsedDate.getTime());
            java.util.Date parsedEndTime = sdf.parse(endTime);
            Time endTimeValue = new Time(parsedEndTime.getTime());
            Date dateValue = Date.valueOf(date);
            // Récupérer le médecin correspondant à l'ID
            Optional<Docteur> docteurOptional = docteurRepository.findById(doctorId);
            if (docteurOptional.isPresent()) {
                Docteur doctor = docteurOptional.get();
                Horaire horaire = new Horaire();
                horaire.setDocteur(doctor);
                horaire.setHeure_debut(startTimeValue);
                horaire.setHeure_fin(endTimeValue);
                horaire.setDate_dispo(dateValue);
                Horaire savedHoraire = horaireRepository.save(horaire);
                // Créer un HoraireDTO à partir des données sauvegardées
                HoraireDTO horaireDTO = new HoraireDTO(
                        (long) savedHoraire.getId_Horaire(), // Utiliser l'identifiant unique de l'horaire
                        doctor.getNom() + " " + doctor.getPrenom(),
                        startTime,
                        endTime,
                        dateValue,
                        doctorId
                );

                // Retourner les détails de l'horaire sauvegardé
                return ResponseEntity.ok(horaireDTO);
            } else {
                return ResponseEntity.badRequest().body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @PostMapping("/api/deleteEvent")
    public ResponseEntity<?> deleteEvent(@RequestParam("eventId") Long eventId) {
        try {
            // Vérifier si l'événement existe dans la base de données
            Optional<Horaire> horaireOptional = horaireRepository.findById(eventId);
            if (horaireOptional.isPresent()) {
                horaireRepository.deleteById(eventId);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/api/updateEvent")
    public ResponseEntity<?> updateEvent(@RequestBody HoraireDTO updatedHoraireDTO) {
        try {
            Long eventId = updatedHoraireDTO.getEventId();
            String doctorName = updatedHoraireDTO.getDoctorName();
            String startTime = updatedHoraireDTO.getStartTime();
            String endTime = updatedHoraireDTO.getEndTime();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            java.util.Date parsedDate = sdf.parse(startTime);
            Time startTimeValue = new Time(parsedDate.getTime());
            java.util.Date parsedEndTime = sdf.parse(endTime);
            Time endTimeValue = new Time(parsedEndTime.getTime());
            Date date = updatedHoraireDTO.getDate();
            int doctorId=updatedHoraireDTO.getDoctorId();
            Optional<Horaire> horaireOptional = horaireRepository.findById(eventId);
            Optional<Docteur> doctor=docteurRepository.findById(doctorId);
            Docteur docteur =doctor.get();
            if (horaireOptional.isPresent()) {
                Horaire horaire = horaireOptional.get();
                horaire.setDocteur(docteur);
                horaire.setHeure_debut(startTimeValue);
                horaire.setHeure_fin(endTimeValue);
                horaire.setDate_dispo(date);
                horaireRepository.save(horaire);

                // Créer un nouveau HoraireDTO avec les données mises à jour
                HoraireDTO updatedDTO = new HoraireDTO(
                        eventId,
                        doctorName,
                        startTime,
                        endTime,
                        date,
                        doctorId
                );

                // Retourner une réponse OK avec le DTO mis à jour
                return ResponseEntity.ok(updatedDTO);
            } else {
                // Retourner une réponse 404 Not Found si l'événement n'est pas trouvé
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Retourner une réponse 500 Internal Server Error en cas d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

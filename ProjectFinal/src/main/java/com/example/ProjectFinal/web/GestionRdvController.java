package com.example.ProjectFinal.web;

import com.example.ProjectFinal.entities.Docteur;
import com.example.ProjectFinal.entities.patientNonAuthetifie;
import com.example.ProjectFinal.entities.rendez_Vous;
import com.example.ProjectFinal.repositories.DocteurRepository;
import com.example.ProjectFinal.repositories.PatientNonAuthetifieRepository;
import com.example.ProjectFinal.repositories.RendezVousRepository;
import com.example.ProjectFinal.service.SmsService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Controller
public class GestionRdvController {

    @Autowired
    private RendezVousRepository rendezVousRepository;
    @Autowired
    private DocteurRepository docteurRepository;
    @Autowired
    private PatientNonAuthetifieRepository patientNonAuthetifieRepository;
    @Autowired
    private SmsService smsService;

    @GetMapping("/rdvsecretaire")
    public String listrdv(@RequestParam(defaultValue = "0") int page,
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
                rendezVousPage = rendezVousRepository.findByDocteurIdAndDateRDVAndStatut(doctorId, sqlDate, PageRequest.of(page, pageSize));
            } catch (ParseException e) {
                // Gérer l'erreur de parsing de la date
                rendezVousPage = rendezVousRepository.findByStatutRDV("en attente", PageRequest.of(page, pageSize));
                model.addAttribute("error", "Erreur lors du parsing de la date.");
            }
        } else {
            // Récupérer tous les rendez-vous sans statut "en attente" si les paramètres ne sont pas fournis
            rendezVousPage = rendezVousRepository.findByStatutRDV("en attente", PageRequest.of(page, pageSize));
        }

        List<Docteur> doctors = docteurRepository.findAll();
        List<patientNonAuthetifie> patients = patientNonAuthetifieRepository.findAll();
        model.addAttribute("doctors", doctors);
        model.addAttribute("patients", patients);
        // Envoyer les données à la vue Thymeleaf
        model.addAttribute("rdvs", rendezVousPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", rendezVousPage.getTotalPages());

        return "rdvsecretaire";
    }
    @PostMapping("/saveRdv")
    public ResponseEntity<String> save(
            @RequestParam(name="patient") String patientN,
            @RequestParam (name="docteur")String docteur,
            @RequestParam String date_RDV ,
            @RequestParam String heure_consultation) {
        rendez_Vous rendezVous = new rendez_Vous();
        Optional<Docteur> optionalDocteur = docteurRepository.findById(Integer.valueOf(docteur));
        Optional<patientNonAuthetifie> p = patientNonAuthetifieRepository.findById(patientN);

        if (optionalDocteur.isPresent() && p.isPresent()) {
            Docteur doctor = optionalDocteur.get();
            patientNonAuthetifie pat = p.get();

            try {
                LocalDate parsedDateRDV = LocalDate.parse(date_RDV, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                rendezVous.setDate_RDV(Date.valueOf(parsedDateRDV));
                rendezVous.setHeure_consultation(Time.valueOf(heure_consultation));
                rendezVous.setPatientNA(pat);
                rendezVous.setDocteur(doctor);
                rendezVous.setStatut_RDV("confirmé");
                rendezVousRepository.save(rendezVous);

                // Sending SMS
                String message = "Votre rendez-vous le " + date_RDV +
                        " à " + heure_consultation + " a été enregistré avec succès !";
                smsService.sendSms(pat.getTel(), message);

                return ResponseEntity.ok( "Votre rendez-vous a été enregistré avec succès ! " );
            } catch (DateTimeParseException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de la conversion de la date du rendez-vous.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Docteur ou patient non trouvé.");
        }
    }

    @PostMapping("/accepter-rendez-vous")
    @ResponseBody
        public void accepterRendezVous(@RequestBody AcceptationRendezVousRequest request) {
        String message = "Votre rendez-vous le " + request.getAppointmentDate() +
                " à " + request.getAppointmentTime() + " a été accepté. Merci!";
        Optional<rendez_Vous> rdv=rendezVousRepository.findById(Integer.valueOf(request.getAppointmentId()));
        rendez_Vous RDV=rdv.get();
        RDV.setStatut_RDV("confirmé");
        rendezVousRepository.save(RDV);
        System.out.println(request.getPatientPhoneNumber());
        smsService.sendSms(request.getPatientPhoneNumber(), message);
    }
    @PostMapping("/refuser-rendez-vous")
    @ResponseBody
    public void refuserRendezVous(@RequestBody RefusRendezVousRequest request) {
        String message = "Votre rendez-vous le " + request.getAppointmentDate() +
                " à " + request.getAppointmentTime() + " a été refusé. Veuillez nous contacter pour plus d'informations.";
        Optional<rendez_Vous> rdv=rendezVousRepository.findById(Integer.valueOf(request.getAppointmentId()));
        rendez_Vous RDV=rdv.get();
        RDV.setStatut_RDV("annulé");
        rendezVousRepository.save(RDV);
        System.out.println(request.getPatientPhoneNumber());
        smsService.sendSms(request.getPatientPhoneNumber(), message);
    }
}

// DTO pour la demande de l'acceptation du rendez-vous
@Setter
@Getter
class AcceptationRendezVousRequest {
    // Getters et setters
    private String appointmentId;
    private String appointmentDate;
    private String appointmentTime;
    private String patientPhoneNumber;

}
@Getter
@Setter
class RefusRendezVousRequest {
    private String appointmentId;
    private String appointmentDate;
    private String appointmentTime;
    private String patientPhoneNumber;
}


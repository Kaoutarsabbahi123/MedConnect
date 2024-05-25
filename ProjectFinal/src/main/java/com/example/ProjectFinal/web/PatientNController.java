package com.example.ProjectFinal.web;



import com.example.ProjectFinal.entities.rendez_Vous;
import com.example.ProjectFinal.repositories.PatientNonAuthetifieRepository;
import com.example.ProjectFinal.entities.patientNonAuthetifie;

import com.example.ProjectFinal.repositories.RendezVousRepository;
import com.example.ProjectFinal.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Controller
public class PatientNController {
    @Autowired
    private PatientNonAuthetifieRepository patientRepository;
    @Autowired
    private RendezVousRepository rendezVousRepository;

    @GetMapping("/patient")
    public String listPatients(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(name = "keyword", defaultValue = "") String keyword,
                               Model model) {
        int pageSize = 10;
        Page<patientNonAuthetifie> patientPage;

        if (keyword.isEmpty()) {
            patientPage = patientRepository.findAll(PageRequest.of(page, pageSize));
        } else {
            // Utiliser la méthode findByNomContains si le mot-clé n'est pas vide
            patientPage = patientRepository.searchByNom(keyword, PageRequest.of(page, pageSize));
        }

        model.addAttribute("patients", patientPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", patientPage.getTotalPages());
        model.addAttribute("pat", new patientNonAuthetifie());
        return "patient";
    }
    @PostMapping("/save2")
    public String save(@ModelAttribute("pat") patientNonAuthetifie patient) {
        patientRepository.save(patient);
        return "redirect:/patient";
    }

    @PutMapping("/archiveEmployeepat/{employeeId}")
    public ResponseEntity<String> archiveEmployee(@PathVariable("employeeId") String employeeId) {
        // Rechercher l'employé par son ID dans la base de données
        patientNonAuthetifie patient = patientRepository.findByCIN(employeeId);
        if (patient == null) {
            // Si le patient n'est pas trouvé, retourner une erreur 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found");
        }

        // Vérifier si le patient a des rendez-vous en attente ou confirmés dans les jours >= aujourd'hui
        List<rendez_Vous> rdvs = rendezVousRepository.findByPatientNAAndStatut_RDVInAndDate_RDVAfter(patient, Arrays.asList("en attente", "Accepté"), LocalDate.now());
        if (!rdvs.isEmpty()) {
            // Si le patient a des rendez-vous en attente ou confirmés dans le futur, retourner une erreur
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Patient has upcoming appointments, cannot be archived");
        }

        // Archiver tous les rendez-vous associés au patient
        for (rendez_Vous rdv : patient.getRDVS()) {
            rdv.setArchive(true);
        }

        // Archiver le patient
        patient.setArchive(true);

        // Enregistrer les modifications dans la base de données
        patientRepository.save(patient);

        // Retourner une réponse OK avec un message de confirmation
        return ResponseEntity.ok("Patient archived successfully");
    }


    @Autowired
    private PatientService patientService;

    @PutMapping("/updateEmployeepat/{id}")
    @ResponseBody
    public String updateEmployee(@RequestBody patientNonAuthetifie updatedPatient) {
        // Call the service method to update patient details
        boolean success = patientService.update(updatedPatient);
        if (success) {
            return "Patient details updated successfully";
        } else {
            return "Failed to update patient details";
        }
    }
}

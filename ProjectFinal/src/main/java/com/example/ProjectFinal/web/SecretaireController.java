package com.example.ProjectFinal.web;


import com.example.ProjectFinal.entities.secretaire;
import com.example.ProjectFinal.repositories.SecretaireRepository;
import com.example.ProjectFinal.service.SecretaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
public class SecretaireController {
    @Autowired
    private SecretaireRepository secretaireRepository;
    @Autowired
    private SecretaireService secretaireService;

    /*@GetMapping("/secretaires")
     public String listSecretairesByCode(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(name = "keyword", required = false) Integer keyword,
                                         Model model) {
         int pageSize = 10;
         Page<secretaire> secretairePage;

         if (keyword != null) {
             secretairePage = secretaireRepository.findByCode(keyword.intValue(), PageRequest.of(page, pageSize));
         } else {
             secretairePage = secretaireRepository.findAll(PageRequest.of(page, pageSize));
         }

         model.addAttribute("secretaires", secretairePage.getContent());
         model.addAttribute("currentPage", page);
         model.addAttribute("totalPages", secretairePage.getTotalPages());
         model.addAttribute("sec", new secretaire());

         return "secretaires";
     }*/
    @GetMapping("/secretaires")
    public String listSecretaires(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(name = "keyword", defaultValue = "") String keyword, Model model) {
        // Nombre d'éléments par page
        int pageSize = 10; // Vous pouvez ajuster cela selon vos besoins
        Page<secretaire> secretairePage;

        if (keyword.isEmpty()) {
            secretairePage = secretaireRepository.findAll(PageRequest.of(page, pageSize));
        } else {
            secretairePage = secretaireRepository.searchByNom(keyword, PageRequest.of(page, pageSize));
        }

        model.addAttribute("secretaires", secretairePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", secretairePage.getTotalPages());
        //model.addAttribute("secretaire", new secretaire());

        return "secretaires";
    }


    @PutMapping("/archiveEmployee/{employeeId}")
    public ResponseEntity<String> archiveEmployee(@PathVariable("employeeId") String employeeId) {
        // Rechercher l'employé par son ID dans la base de données
        secretaire employee = secretaireRepository.findByLogin(employeeId);
        if (employee == null) {
            // Si l'employé n'est pas trouvé, retourner une erreur 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }

        // Archiver l'employé en définissant l'attribut archive sur true
        employee.setArchive(true);

        // Enregistrer les modifications dans la base de données
        secretaireRepository.save(employee);

        // Retourner une réponse OK avec un message de confirmation
        return ResponseEntity.ok("Employee archived successfully");
    }

    @PutMapping("/updateEmployee/{id}")
    @ResponseBody
    public String updateEmployee(@RequestBody secretaire updatedsecretaire) {
        updatedsecretaire.setRole("Secrétaire");
        // Call the service method to update employee details
        boolean success = secretaireService.update(updatedsecretaire);
        if (success) {
            return "Employee details updated successfully";
        } else {
            return "Failed to update employee details";
        }
    }

    @PostMapping("/save1")
    @ResponseBody
    public ResponseEntity<?> savespec(
            @RequestParam("login") String login,
            @RequestParam("code_secretaire") String codeSecretaire,
            @RequestParam("CIN") String cin,
            @RequestParam("nom") String nom,
            @RequestParam("Prenom") String prenom,
            @RequestParam("sexe") String sexe,
            @RequestParam("dat_naiss") String datNaiss,
            @RequestParam("tel") String tel,
            @RequestParam("address") String address,
            @RequestParam("mot_passe") String motPasse) {

        try {
            secretaire secretaire = new secretaire();
            secretaire.setLogin(login);
            secretaire.setCode(Integer.valueOf(codeSecretaire));
            secretaire.setCIN(cin);
            secretaire.setNom(nom);
            secretaire.setPrenom(prenom);
            secretaire.setSexe(sexe);
            LocalDate parsedDateNaiss = LocalDate.parse(datNaiss, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            secretaire.setDat_naiss(Date.valueOf(parsedDateNaiss));
            secretaire.setTel(tel);
            secretaire.setAddress(address);
            secretaire.setMot_passe(motPasse);
            secretaire.setRole("Secrétaire");

            secretaireRepository.save(secretaire);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}

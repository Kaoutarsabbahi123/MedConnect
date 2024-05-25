package com.example.ProjectFinal.web;


import com.example.ProjectFinal.entities.Specialité;
import com.example.ProjectFinal.repositories.SpecialiteRepository;
import com.example.ProjectFinal.service.SpecialiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class SpecController {
    @Autowired
    private SpecialiteRepository specialiteRepository;
    @Autowired
    private SpecialiteService specialiteService;

    @GetMapping("/api/specialites")
    @ResponseBody
    public List<Specialité> getSpecialités() {
        return specialiteRepository.findAll();
    }

    @GetMapping("/specialites")
    public String listSpecialites(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(name = "keyword", defaultValue = "") String keyword, Model model) {
        // Nombre d'éléments par page
        int pageSize = 10; // Vous pouvez ajuster cela selon vos besoins
        Page<Specialité> specialitéPage;
        // Récupérer la page de docteurs depuis la base de données
        if (keyword.isEmpty()) {
            specialitéPage = specialiteRepository.findAll(PageRequest.of(page, pageSize));
        } else {
            specialitéPage = specialiteRepository.findByNomContains(keyword, PageRequest.of(page, pageSize));
        }
        // Envoyer les données à la vue Thymeleaf
        model.addAttribute("specialites", specialitéPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", specialitéPage.getTotalPages());
        model.addAttribute("specialite", new Specialité());
        return "specialite";
    }


    @PostMapping("/saveSpecialite")
    public String saveSpecialite(@ModelAttribute("specialite") Specialité specialité) {
        specialiteRepository.save(specialité);
        return "redirect:/specialites";
    }


    @PutMapping("/archiveEmployeespec/{employeeId}")
    public ResponseEntity<String> archiveEmployee(@PathVariable("employeeId") Integer employeeId) {
        // Rechercher l'employé par son ID dans la base de données
        Optional<Specialité> employeeOptional = specialiteRepository.findById(employeeId);

        // Vérifier si l'employé existe
        if (employeeOptional.isEmpty()) {
            // Si l'employé n'est pas trouvé, retourner une erreur 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }

        // Obtenir l'objet Employee à partir de l'Optional
        Specialité employee = employeeOptional.get();

        // Archiver l'employé en définissant l'attribut archive sur true
        employee.setArchive(true);

        // Enregistrer les modifications dans la base de données
        specialiteRepository.save(employee);

        // Retourner une réponse OK avec un message de confirmation
        return ResponseEntity.ok("Employee archived successfully");
    }



    @PutMapping("/updateEmployeespec/{id}") // Mettez à jour le mapping pour inclure l'ID de l'employé
    @ResponseBody
    public String updateEmployee(@RequestBody Specialité updatedSpecialite, @PathVariable("id") Integer id) { // Ajoutez une variable de chemin pour l'ID de l'employé
        updatedSpecialite.setId(id); // Définir l'ID de l'employé à partir du chemin
        boolean success = specialiteService.update(updatedSpecialite); // Utilisation du service pour mettre à jour le docteur
        if (success) {
            return "Employee details updated successfully";
        } else {
            return "Failed to update employee details";
        }
    }


}

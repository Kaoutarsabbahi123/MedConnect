package com.example.ProjectFinal.web;



import com.example.ProjectFinal.entities.Docteur;
import com.example.ProjectFinal.entities.Specialité;
import com.example.ProjectFinal.repositories.DocteurRepository;
import com.example.ProjectFinal.repositories.SpecialiteRepository;
import com.example.ProjectFinal.service.Docteurservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



import java.util.List;

@Controller
public class CrudDocController {

    @Autowired
    private DocteurRepository docteurRepository;
    @Autowired
    private SpecialiteRepository specialiteRepository;
    @Autowired
    private Docteurservice docteurService;
    @GetMapping("/docteur")
    public String listDocteurs(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(name = "keyword", defaultValue = "") String keyword, Model model) {
        // Nombre d'éléments par page
        int pageSize = 10; // Vous pouvez ajuster cela selon vos besoins
        Page<Docteur> docteurPage;
        List<Specialité> spécialités = specialiteRepository.findAll();
        // Récupérer la page de docteurs depuis la base de données
        if (keyword.isEmpty()) {
        docteurPage = docteurRepository.findAll(PageRequest.of(page, pageSize));
        } else {
            docteurPage = docteurRepository.searchByNom(keyword, PageRequest.of(page, pageSize));
        }
        // Envoyer les données à la vue Thymeleaf
        model.addAttribute("docteurs", docteurPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", docteurPage.getTotalPages());
        model.addAttribute("doc", new Docteur());
        model.addAttribute("specialites", spécialités);
        return "docteur";
    }
    @PostMapping("/save")
    public String save(@ModelAttribute("doc") Docteur docteur) {
        Integer specialiteId = docteur.getSpecialite().getId();

        // Récupérer l'objet Specialité correspondant à l'ID choisi
        Specialité specialité = specialiteRepository.findById(specialiteId).orElse(null);

        // Attribuer la spécialité choisie au docteur
        docteur.setSpecialite(specialité);
        docteurRepository.save(docteur);
        return "redirect:/docteur";
    }


    @PutMapping("/archiveEmployeedoc/{employeeId}")
    public ResponseEntity<String> archiveEmployee(@PathVariable("employeeId") Integer employeeId) {
        // Rechercher l'employé par son ID dans la base de données
        Docteur employee = docteurRepository.findById_docteur(employeeId);
        if (employee == null) {
            // Si l'employé n'est pas trouvé, retourner une erreur 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }

        // Archiver l'employé en définissant l'attribut archive sur true
        employee.setArchive(true);

        // Enregistrer les modifications dans la base de données
        docteurRepository.save(employee);

        // Retourner une réponse OK avec un message de confirmation
        return ResponseEntity.ok("Employee archived successfully");
    }


    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, value = "/updateEmployeedoc/{id}")
    @ResponseBody
    public String updateEmployee(@RequestBody Docteur updatedDocteur, @PathVariable("id") Integer id,@RequestParam("specialiteId") Integer specialiteId) { // Ajoutez une variable de chemin pour l'ID de l'employé
        updatedDocteur.setId_docteur(id); // Définir l'ID de l'employé à partir du chemin
        System.out.println(specialiteId);
        updatedDocteur.setSpecialite(specialiteRepository.findByIdS(specialiteId));

        boolean success = docteurService.update(updatedDocteur); // Utilisation du service pour mettre à jour le docteur
        if (success) {
            return "Employee details updated successfully";
        } else {
            return "Failed to update employee details";
        }
    }

}

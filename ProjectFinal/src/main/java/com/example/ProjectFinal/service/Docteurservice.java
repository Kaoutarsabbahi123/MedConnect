package com.example.ProjectFinal.service;


import com.example.ProjectFinal.entities.Docteur;
import com.example.ProjectFinal.repositories.DocteurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service // Déclaration du service comme un bean Spring
public class Docteurservice {

    @Autowired
    private DocteurRepository docteurRepository;

    public boolean update(Docteur docteur) {
        docteurRepository.save(docteur);
        return true;
    }

    public int getNombreMedecins() {
        // Utiliser la méthode count() fournie par Spring Data JPA
        return (int) docteurRepository.count();
    }
}

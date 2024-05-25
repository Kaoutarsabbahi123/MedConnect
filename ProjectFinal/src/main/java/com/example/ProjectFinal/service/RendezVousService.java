package com.example.ProjectFinal.service;

import com.example.ProjectFinal.repositories.RendezVousRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RendezVousService {

    @Autowired
    private RendezVousRepository rendezVousRepository;

    public int getNombreRendezVous() {
        // Utiliser la méthode count() fournie par Spring Data JPA
        return (int) rendezVousRepository.count();
    }
}
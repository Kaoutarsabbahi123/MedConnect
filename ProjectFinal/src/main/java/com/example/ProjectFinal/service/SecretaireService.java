package com.example.ProjectFinal.service;

import com.example.ProjectFinal.entities.User;
import com.example.ProjectFinal.entities.secretaire;
import com.example.ProjectFinal.repositories.SecretaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecretaireService {
    @Autowired
    private SecretaireRepository secretaireRepository;
    public boolean update(secretaire secretairee) {
        secretaireRepository.save(secretairee);
        return true;
    }
}

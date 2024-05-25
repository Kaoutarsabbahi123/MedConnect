package com.example.ProjectFinal.service;


import com.example.ProjectFinal.entities.Specialité;
import com.example.ProjectFinal.entities.secretaire;
import com.example.ProjectFinal.repositories.SpecialiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpecialiteService {
    @Autowired
    private SpecialiteRepository specialiteRepository;
    public  boolean update(Specialité specialité) {
        specialiteRepository.save(specialité);
        return true;
    }


}

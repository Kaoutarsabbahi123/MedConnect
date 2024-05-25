package com.example.ProjectFinal.service;

import com.example.ProjectFinal.repositories.PatientNonAuthetifieRepository;
import com.example.ProjectFinal.entities.patientNonAuthetifie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientService {
    @Autowired
    private PatientNonAuthetifieRepository patientRepository;
    public boolean update(patientNonAuthetifie patient) {
        patientRepository.save(patient);
        return true;
    }
    public int getNombrePatients() {
        // Utiliser la méthode count() fournie par Spring Data JPA
        return (int) patientRepository.count();
    }
}

package com.example.ProjectFinal.repositories;

import com.example.ProjectFinal.entities.patientNonAuthetifie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientNonAuthetifieRepository extends JpaRepository<patientNonAuthetifie,String> {
}

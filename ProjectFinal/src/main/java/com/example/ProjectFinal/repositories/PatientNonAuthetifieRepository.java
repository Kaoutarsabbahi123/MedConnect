package com.example.ProjectFinal.repositories;

import com.example.ProjectFinal.entities.patientNonAuthetifie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PatientNonAuthetifieRepository extends JpaRepository<patientNonAuthetifie,String> {
    @Query("SELECT p FROM patientNonAuthetifie p WHERE p.Nom LIKE %:keyword%")
    Page<patientNonAuthetifie> searchByNom(@Param("keyword") String keyword, Pageable pageable);

    patientNonAuthetifie findByCIN(String CIN);
}

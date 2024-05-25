package com.example.ProjectFinal.repositories;

import com.example.ProjectFinal.entities.Docteur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DocteurRepository extends JpaRepository<Docteur,Integer> {
    Page<Docteur> findByNomContains(String Nom, Pageable pageable);
    @Query("SELECT d FROM Docteur d WHERE d.id_docteur = ?1")
    Docteur findById_docteur(int id_docteur);

    @Query("SELECT d FROM Docteur d WHERE d.Nom LIKE %:keyword%")
    Page<Docteur> searchByNom(@Param("keyword") String keyword, Pageable pageable);

}

package com.example.ProjectFinal.repositories;

import com.example.ProjectFinal.entities.secretaire;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface SecretaireRepository extends JpaRepository<secretaire, String> {

    Page<secretaire> findByCode(int code, Pageable pageable);

    secretaire findByLogin(String login);
    @Query("SELECT s FROM secretaire s WHERE s.Nom LIKE %:keyword%")
    Page<secretaire> searchByNom(@Param("keyword") String keyword, Pageable pageable);
}


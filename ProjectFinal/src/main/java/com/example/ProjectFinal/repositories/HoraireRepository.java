package com.example.ProjectFinal.repositories;

import com.example.ProjectFinal.entities.Docteur;
import com.example.ProjectFinal.entities.Horaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.sql.Time;
import java.util.Optional;

public interface HoraireRepository extends JpaRepository<Horaire, Long> {

    @Query("SELECT h FROM Horaire h WHERE h.docteur = :docteur " +
            "AND h.heure_debut = :heureDebut " +
            "AND h.heure_fin = :heureFin " +
            "AND h.date_dispo = :dateDispo")
    Optional<Horaire> findByDocteurAndHeureDebutAndHeureFinAndDateDispo(
            @Param("docteur") Docteur docteur,
            @Param("heureDebut") Time heureDebut,
            @Param("heureFin") Time heureFin,
            @Param("dateDispo") Date dateDispo
    );
}

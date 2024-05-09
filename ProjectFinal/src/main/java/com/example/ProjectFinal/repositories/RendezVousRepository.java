package com.example.ProjectFinal.repositories;

import com.example.ProjectFinal.entities.Docteur;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ProjectFinal.entities.rendez_Vous;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.sql.Time;

public interface RendezVousRepository extends JpaRepository<rendez_Vous, Integer> {
    @Query("SELECT COUNT(r) FROM rendez_Vous r WHERE r.docteur = :docteur AND r.date_RDV = :date_RDV AND r.heure_consultation = :heure_consultation")
    long countByDocteurAndDateRDVAndHeureConsultation(@Param("docteur") Docteur docteur,
                                                      @Param("date_RDV") Date date_RDV,
                                                      @Param("heure_consultation") Time heure_consultation);


}


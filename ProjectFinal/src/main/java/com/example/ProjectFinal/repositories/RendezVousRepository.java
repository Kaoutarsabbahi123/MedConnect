package com.example.ProjectFinal.repositories;

import com.example.ProjectFinal.entities.Docteur;
import com.example.ProjectFinal.entities.patientNonAuthetifie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ProjectFinal.entities.rendez_Vous;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

public interface RendezVousRepository extends JpaRepository<rendez_Vous, Integer> {
    @Query("SELECT COUNT(r) FROM rendez_Vous r " +
            "WHERE r.docteur = :docteur " +
            "AND r.date_RDV = :date_RDV " +
            "AND r.heure_consultation = :heure_consultation " +
            "AND r.statut_RDV = 'en attente' " +
            "AND r.archive = false")
    long countByDocteurAndDateRDVAndHeureConsultationAndStatutRDV(@Param("docteur") Docteur docteur,
                                                               @Param("date_RDV") Date date_RDV,
                                                               @Param("heure_consultation") Time heure_consultation);


    @Query("SELECT r FROM rendez_Vous r WHERE r.statut_RDV <> ?1")
    Page<rendez_Vous> findByStatutRDVNot(String statut, Pageable pageable);
    @Query("SELECT r FROM rendez_Vous r WHERE r.docteur.id_docteur = :doctorId " +
            "AND r.date_RDV = :dateRDV AND r.statut_RDV <> 'en attente'")
    Page<rendez_Vous> findByDocteurIdAndDateRDVAndStatutNot(int doctorId, Date dateRDV, Pageable pageable);
    @Query("SELECT r FROM rendez_Vous r WHERE r.patientNA = :patientNA " +
            "AND (r.statut_RDV IN :statuts OR r.date_RDV > :date OR " +
            "(SELECT COUNT(rv) FROM rendez_Vous rv WHERE rv.patientNA = :patientNA) = 0)")
    List<rendez_Vous> findByPatientNAAndStatut_RDVInAndDate_RDVAfter(
            @Param("patientNA") patientNonAuthetifie patientNA,
            @Param("statuts") List<String> statuts,
            @Param("date") LocalDate date
    );
    @Query("SELECT r FROM rendez_Vous r WHERE r.statut_RDV ='en attente'")
    Page<rendez_Vous> findByStatutRDV(String statut, Pageable pageable);
    @Query("SELECT r FROM rendez_Vous r WHERE r.docteur.id_docteur = :doctorId " +
            "AND r.date_RDV = :dateRDV AND r.statut_RDV = 'en attente'")
    Page<rendez_Vous> findByDocteurIdAndDateRDVAndStatut(int doctorId, Date dateRDV, Pageable pageable);
}


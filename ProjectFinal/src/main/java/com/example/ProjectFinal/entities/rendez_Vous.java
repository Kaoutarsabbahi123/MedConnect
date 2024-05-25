package com.example.ProjectFinal.entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class rendez_Vous implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_RDV;
    private Date date_RDV;
    @Temporal(TemporalType.TIME)
    private Time heure_consultation;
    private String statut_RDV;
    private boolean archive;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="CIN")
    private patientNonAuthetifie patientNA;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="id_docteur")
    private Docteur docteur;

}

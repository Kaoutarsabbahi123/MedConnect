package com.example.ProjectFinal.entities;
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
public class Horaire implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_Horaire;
    private Date date_dispo;
    @Temporal(TemporalType.TIME)
    private Time heure_debut;
    @Temporal(TemporalType.TIME)
    private Time heure_fin;
    @ManyToOne
    @JoinColumn(name="id_docteur")
    private Docteur docteur;

}

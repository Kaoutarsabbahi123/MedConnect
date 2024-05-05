package com.example.ProjectFinal.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Docteur implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_docteur;
    private String CIN;
    private String Nom;
    private String Prenom;
    private String Sexe;
    private String address;
    private String tel;
    private Date dat_naiss;
    private String email;
    @OneToMany(mappedBy="docteur")
    private List<rendez_Vous> RDVS;
    @OneToMany(mappedBy="docteur")
    private List<Horaire> horaires;
    @ManyToOne
    @JoinColumn(name="id_specialite")
    private Specialité Specialite;
    @Override
    public String toString() {
        return "Docteur{id=" + id_docteur + ", nom='" + Nom + "', prenom='" + Prenom + "'}";
    }
}


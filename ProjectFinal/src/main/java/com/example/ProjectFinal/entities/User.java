package com.example.ProjectFinal.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements Serializable {
    @Id
    private String login;
    private String CIN;
    private String mot_passe;
    private String role;
    private String Nom;
    private String Prenom;
    private String Sexe;
    private String address;
    private String tel;
    private Date dat_naiss;
}

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
public class patientNonAuthetifie implements Serializable{
    @Id
    private String CIN;
    private String Nom;
    private String Prenom;
    private String Sexe;
    private String address;
    private String tel;
    private Date dat_naiss;
    private String email;
    @OneToMany(mappedBy="patientNA")
    private List<rendez_Vous> RDVS;
}

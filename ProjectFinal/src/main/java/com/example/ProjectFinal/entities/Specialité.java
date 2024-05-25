package com.example.ProjectFinal.entities;
import com.example.ProjectFinal.entities.Docteur;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Specialité implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom;
    private boolean archive;
    @JsonManagedReference
    @OneToMany(mappedBy="specialite")
    private List<Docteur> docteurs;
}

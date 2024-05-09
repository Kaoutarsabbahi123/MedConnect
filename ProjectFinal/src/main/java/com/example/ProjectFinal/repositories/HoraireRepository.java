package com.example.ProjectFinal.repositories;

import com.example.ProjectFinal.entities.Horaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;

public interface HoraireRepository extends JpaRepository<Horaire,Long> {

}

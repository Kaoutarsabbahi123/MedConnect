package com.example.ProjectFinal.repositories;

import com.example.ProjectFinal.entities.Docteur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocteurRepository extends JpaRepository<Docteur,Integer> {

}

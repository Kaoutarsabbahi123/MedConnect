package com.example.ProjectFinal.repositories;

import com.example.ProjectFinal.entities.Docteur;
import com.example.ProjectFinal.entities.Specialité;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialiteRepository extends JpaRepository<Specialité,Integer> {
}

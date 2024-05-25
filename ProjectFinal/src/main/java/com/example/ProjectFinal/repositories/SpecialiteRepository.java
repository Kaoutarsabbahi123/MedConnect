package com.example.ProjectFinal.repositories;

import com.example.ProjectFinal.entities.Docteur;
import com.example.ProjectFinal.entities.Specialité;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SpecialiteRepository extends JpaRepository<Specialité,Integer> {
    Optional<Specialité> findById(Integer integer);
    @Query("SELECT S FROM Specialité S WHERE S.id = ?1")
    Specialité findByIdS(Integer id);
    Page<Specialité> findByNomContains(String nom, Pageable pageable);
}

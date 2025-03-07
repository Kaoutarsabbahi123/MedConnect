package com.example.ProjectFinal.repositories;

import com.example.ProjectFinal.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByLogin(String login);
}

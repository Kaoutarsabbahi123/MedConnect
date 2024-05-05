package com.example.ProjectFinal.service;

import com.example.ProjectFinal.entities.User;
import com.example.ProjectFinal.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    public void update(User user) {
        userRepository.save(user);
    }

}

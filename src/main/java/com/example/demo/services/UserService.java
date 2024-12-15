package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.user;
import com.example.demo.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Get all users
    public List<user> getAllUsers() {
        return userRepository.findAll();
    }

    // Get a user by ID
    public Optional<user> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Create a new user
    public user createUser(user user) {
        return userRepository.save(user);
    }

    // Delete a user
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public user loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
        .orElseThrow(null);
    }
}
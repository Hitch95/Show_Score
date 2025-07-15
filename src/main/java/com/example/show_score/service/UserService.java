package com.example.show_score.service;

import com.example.show_score.model.UserBean;
import com.example.show_score.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserBean findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // Method for hashing password on creation/update
    public UserBean save(UserBean user) {
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    // Only ADMIN can realize CRUD operations on users

    // Create a new user
    public UserBean createUser(UserBean user) {
        if (user == null || user.getUsername() == null || user.getEmail() == null || user.getPassword() == null) {
            throw new RuntimeException("User cannot be null or empty");
        }
        System.out.println("createUser : " + user);
        return userRepository.save(user);
    }

    // Read operations
    public List<UserBean> getAllUsers() {
        return userRepository.findAll();
    }

    public UserBean getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserBean getByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    // Update an existing user
    public UserBean updateUser(UserBean user) {
        if (user == null || user.getId() == null) {
            throw new RuntimeException("User or User ID cannot be null");
        }
        System.out.println("updateUser : " + user);
        return userRepository.save(user);
    }

    // Delete
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    // Authentication method with BCrypt
    public UserBean authenticate(String username, String password) {
        UserBean user = userRepository.findByUsername(username).orElse(null);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null; // Authentication failed
    }
}

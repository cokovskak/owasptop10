package com.example.owasp.service;

import com.example.owasp.model.User;
import com.example.owasp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        System.out.println("Password in base64: "+user.getPassword());
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public boolean usernameExists(String username) {
       return userRepository.existsByUsername(username);
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
    public String getUsernameById(Long id) {
        return userRepository.findById(id).get().getUsername();
    }
}
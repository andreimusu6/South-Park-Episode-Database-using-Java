package com.south.park.service;

import com.south.park.entity.UserEntity;
import com.south.park.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy // Previne eroarea de "Circular Dependency" la pornire
    private PasswordEncoder passwordEncoder;

    // 1. Logica de Login (folosită de Spring Security)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().replace("ROLE_", ""))
                .build();
    }

    // 2. Logica de Înregistrare
    public void saveUser(String username, String rawPassword) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Userul există deja!");
        }

        UserEntity newUser = new UserEntity();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(rawPassword));
        newUser.setRole("ROLE_USER");
        userRepository.save(newUser);
    }

    // 3. Crearea Automată a Adminului
    @PostConstruct
    public void initAdmin() {
        String adminUsername = "admin"; // <--- Pune aici username-ul dorit pentru admin

        if (userRepository.findByUsername(adminUsername).isPresent()) {
            return; // Dacă există, nu facem nimic
        }

        UserEntity admin = new UserEntity();
        admin.setUsername(adminUsername);
        admin.setPassword(passwordEncoder.encode("pass")); // <--- Pune aici parola dorită
        admin.setRole("ROLE_ADMIN");

        userRepository.save(admin);
        System.out.println("✅ ADMIN NOU CREAT: " + adminUsername);
    }

    // 4. Returnează toți userii (pentru Admin Panel)
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    // 5. Șterge un user după ID (NOU)
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
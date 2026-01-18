package com.south.park.controller;

import com.south.park.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // 1. Afișează pagina de Login
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // 2. Afișează pagina de Înregistrare (NOU)
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register"; // Trebuie să creăm register.html
    }

    // 3. Procesează datele de Înregistrare (NOU)
    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password) {
        try {
            // Încercăm să salvăm userul
            userService.saveUser(username, password);

            // Dacă reușim, trimitem userul la login cu un mesaj de succes
            return "redirect:/login?registered";
        } catch (Exception e) {
            // Dacă userul există deja (UserService aruncă eroare),
            // îl trimitem înapoi la register cu un parametru de eroare
            return "redirect:/register?error";
        }
    }
}
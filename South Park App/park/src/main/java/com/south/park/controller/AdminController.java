package com.south.park.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.south.park.entity.DataEntity;
import com.south.park.entity.UserEntity;
import com.south.park.model.EpisodModel;
import com.south.park.repository.EpisodRepository;
import com.south.park.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal; // Import necesar pentru a vedea cine e logat
import java.util.Base64;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private EpisodRepository episodRepository;

    @Autowired
    private UserService userService;

    private final ObjectMapper mapper = new ObjectMapper();

    // ----------------------------------------------------
    // ZONA DE EPISOADE (TAB 1)
    // ----------------------------------------------------

    @GetMapping
    public String adminPanel(Model model) {
        model.addAttribute("episoade", episodRepository.findAll());
        return "admin/dashboard";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("episod", new EpisodModel());
        model.addAttribute("dbId", null);
        return "admin/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            DataEntity entity = episodRepository.findById(id).orElseThrow();
            EpisodModel episod = mapper.readValue(entity.getJsonContent(), EpisodModel.class);
            model.addAttribute("episod", episod);
            model.addAttribute("dbId", id);
            return "admin/form";
        } catch (Exception e) {
            return "redirect:/admin";
        }
    }

    @PostMapping("/save")
    public String saveEpisode(
            @ModelAttribute EpisodModel episodModel,
            @RequestParam("imageFile") MultipartFile imageFile,
            @RequestParam(value = "dbId", required = false) Long dbId
    ) {
        try {
            if (!imageFile.isEmpty()) {
                byte[] bytes = imageFile.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(bytes);
                episodModel.setImagine("data:image/png;base64," + base64Image);
            } else if (dbId != null) {
                DataEntity oldEntity = episodRepository.findById(dbId).orElseThrow();
                EpisodModel oldModel = mapper.readValue(oldEntity.getJsonContent(), EpisodModel.class);
                episodModel.setImagine(oldModel.getImagine());
            }

            String jsonString = mapper.writeValueAsString(episodModel);
            DataEntity entity = (dbId != null) ? episodRepository.findById(dbId).orElse(new DataEntity()) : new DataEntity();
            entity.setJsonContent(jsonString);
            episodRepository.save(entity);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteEpisode(@PathVariable Long id) {
        episodRepository.deleteById(id);
        return "redirect:/admin";
    }

    // ----------------------------------------------------
    // ZONA DE UTILIZATORI (TAB 2)
    // ----------------------------------------------------

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    // ȘTERGERE UTILIZATOR (CU PROTECȚIE SELF-DELETE)
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, Principal principal) {
        // Căutăm userul pe care vrem să îl ștergem
        UserEntity userDeSters = userService.getAllUsers().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (userDeSters != null) {
            String loggedInUsername = principal.getName();

            // Verificăm dacă încerci să te ștergi pe tine
            if (userDeSters.getUsername().equals(loggedInUsername)) {
                return "redirect:/admin/users?error=self_delete";
            }

            userService.deleteUserById(id);
        }

        return "redirect:/admin/users";
    }
}
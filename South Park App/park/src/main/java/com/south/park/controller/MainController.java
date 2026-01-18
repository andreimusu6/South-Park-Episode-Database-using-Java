package com.south.park.controller;

import com.south.park.model.EpisodModel;
import com.south.park.service.EpisodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class MainController {

    @Autowired
    private EpisodService service;

    /**
     * PAGINA PRINCIPALĂ (index.html)
     * Gestionează afișarea, căutarea, filtrarea și paginarea.
     */
    @GetMapping("/")
    public String homepage(
            Model model,
            // Parametrii din URL (ex: ?search=text&season=1&episode=5&page=2)
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer season,
            @RequestParam(required = false) Integer episode, // Parametrul nou pentru nr. episod
            @RequestParam(defaultValue = "1") int page,      // Pagina curentă (default 1)
            @RequestParam(defaultValue = "6") int size       // Câte elemente pe pagină (default 6)
    ) {
        // 1. Apelăm Service-ul pentru a obține lista filtrată și paginată
        // Service-ul face toată munca grea (căutare în JSON, calcule paginare)
        EpisodService.PageResult<EpisodModel> result = service.getEpisodes(search, season, episode, page, size);

        // 2. Trimitem datele necesare către HTML
        model.addAttribute("episoadeKey", result.content); // Lista de episoade pentru carduri
        model.addAttribute("currentPage", page);           // Pagina la care ne aflăm
        model.addAttribute("totalPages", result.totalPages); // Câte pagini sunt în total
        model.addAttribute("totalItems", result.totalItems); // Câte episoade am găsit în total

        // 3. Trimitem parametrii înapoi în pagină
        // Asta face ca după ce dai click pe "Go", valorile să rămână scrise în căsuțe
        model.addAttribute("searchParam", search);
        model.addAttribute("seasonParam", season);
        model.addAttribute("episodeParam", episode);
        model.addAttribute("sizeParam", size);

        // 4. Generăm lista de sezoane pentru dropdown (de la 1 la 30)
        // Asta ne scutește să scriem 30 de <option> manual în HTML
        List<Integer> sezoane = IntStream.rangeClosed(1, 30).boxed().collect(Collectors.toList());
        model.addAttribute("allSeasons", sezoane);

        return "index"; // Returnează templates/index.html
    }

    /**
     * PAGINA DE DETALII (detalii.html)
     * Afișează un singur episod pe baza ID-ului din baza de date.
     */
    @GetMapping("/detalii/{id}")
    public String detalii(@PathVariable Long id, Model model) {
        // Căutăm episodul după ID
        EpisodModel episod = service.getEpisodeById(id);

        // Dacă nu există (cineva a scris un ID greșit în URL), redirecționăm acasă
        if (episod == null) {
            return "redirect:/";
        }

        // Trimitem obiectul episod către pagina de detalii
        model.addAttribute("episod", episod);

        return "detalii"; // Returnează templates/detalii.html
    }
}
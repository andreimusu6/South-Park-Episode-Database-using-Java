package com.south.park.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.south.park.entity.DataEntity;
import com.south.park.model.EpisodModel;
import com.south.park.repository.EpisodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EpisodService {

    @Autowired
    private EpisodRepository repository;

    private final ObjectMapper mapper = new ObjectMapper();

    // Metoda principală care face tot: Căutare + Filtrare + Paginare
    // Modificăm semnătura metodei să primească și 'Integer episode'
    public PageResult<EpisodModel> getEpisodes(String search, Integer season, Integer episode, int page, int size) {

        List<DataEntity> entities = repository.findAll();
        List<EpisodModel> allEpisodes = new ArrayList<>();

        for (DataEntity entity : entities) {
            try {
                EpisodModel model = mapper.readValue(entity.getJsonContent(), EpisodModel.class);
                model.setId(entity.getId());
                allEpisodes.add(model);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 3. FILTRARE ACTUALIZATĂ
        List<EpisodModel> filtered = allEpisodes.stream()
                // Filtru Sezon
                .filter(e -> season == null || (e.getSezon() != null && e.getSezon().equals(season)))
                // Filtru NOU Episod
                .filter(e -> episode == null || (e.getNumarEpisod() != null && e.getNumarEpisod().equals(episode)))
                // Căutare Generală
                .filter(e -> search == null || search.isEmpty() ||
                        (e.getTitlu() != null && e.getTitlu().toLowerCase().contains(search.toLowerCase())) ||
                        (e.getDescriere() != null && e.getDescriere().toLowerCase().contains(search.toLowerCase())))
                .collect(Collectors.toList());

        // 4. PAGINARE (Rămâne la fel)
        int totalItems = filtered.size();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        int start = Math.min((page - 1) * size, totalItems);
        int end = Math.min(start + size, totalItems);

        List<EpisodModel> pageItems = filtered.subList(start, end);

        return new PageResult<>(pageItems, totalPages, page, totalItems);
    }

    // Metoda pentru Detalii
    public EpisodModel getEpisodeById(Long id) {
        DataEntity entity = repository.findById(id).orElse(null);
        if (entity == null) return null;
        try {
            EpisodModel model = mapper.readValue(entity.getJsonContent(), EpisodModel.class);
            model.setId(entity.getId());
            return model;
        } catch (Exception e) {
            return null;
        }
    }

    // Clasă ajutătoare internă (DTO) pentru a trimite datele la Controller
    public static class PageResult<T> {
        public List<T> content;
        public int totalPages;
        public int currentPage;
        public int totalItems;

        public PageResult(List<T> content, int totalPages, int currentPage, int totalItems) {
            this.content = content;
            this.totalPages = totalPages;
            this.currentPage = currentPage;
            this.totalItems = totalItems;
        }
    }
}
package com.south.park.model; // Verifică pachetul

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) // Ignoră câmpurile din JSON pe care nu le folosim
public class EpisodModel {

    private Long id; // <--- ADAUGĂ ASTA (Nu e in JSON, il punem noi manual)
    // Aici mapăm cheile din JSON la variabile Java

    @JsonProperty("name") // Așa apare în JSON
    private String titlu;

    @JsonProperty("season")
    private Integer sezon;

    @JsonProperty("episode")
    private Integer numarEpisod;

    @JsonProperty("description")
    private String descriere;

    @JsonProperty("image")
    private String imagine; // Aici va fi string-ul Base64 pentru poză

    // Getters și Setters (Click Dreapta -> Generate -> Getter and Setter)

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitlu() { return titlu; }
    public void setTitlu(String titlu) { this.titlu = titlu; }

    public Integer getSezon() { return sezon; }
    public void setSezon(Integer sezon) { this.sezon = sezon; }

    public Integer getNumarEpisod() { return numarEpisod; }
    public void setNumarEpisod(Integer numarEpisod) { this.numarEpisod = numarEpisod; }

    public String getDescriere() { return descriere; }
    public void setDescriere(String descriere) { this.descriere = descriere; }

    public String getImagine() { return imagine; }
    public void setImagine(String imagine) { this.imagine = imagine; }
}
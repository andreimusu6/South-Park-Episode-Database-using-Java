package com.south.park.detalii;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EpisodDetalii {
    // Mapăm numele din JSON la variabile Java

    @JsonProperty("name")
    private String titlu;

    @JsonProperty("season")
    private Integer sezon;

    @JsonProperty("episode")
    private Integer numarEpisod;

    @JsonProperty("description")
    private String descriere;

    @JsonProperty("image")
    private String imagineBase64; // Asta conține poza

    // Getters și Setters (Poți folosi Alt+Insert în IntelliJ)
    public String getTitlu() { return titlu; }
    public void setTitlu(String titlu) { this.titlu = titlu; }

    public Integer getSezon() { return sezon; }
    public void setSezon(Integer sezon) { this.sezon = sezon; }

    public String getDescriere() { return descriere; }
    public void setDescriere(String descriere) { this.descriere = descriere; }

    public String getImagineBase64() { return imagineBase64; }
    public void setImagineBase64(String imagineBase64) { this.imagineBase64 = imagineBase64; }
}
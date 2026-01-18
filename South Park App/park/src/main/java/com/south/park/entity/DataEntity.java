package com.south.park.entity; // Verifică să fie numele pachetului tău

import jakarta.persistence.*;

@Entity
@Table(name = "data") // Numele tabelului din SQL-ul tău este "data"
public class DataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Coloana din SQL se numește tot "data" și este de tip JSON.
    // Noi o vom citi ca un String lung (text) în Java.
    @Column(name = "data", columnDefinition = "json")
    private String jsonContent;

    // Constructor gol obligatoriu
    public DataEntity() {
    }

    // Getters și Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJsonContent() {
        return jsonContent;
    }

    public void setJsonContent(String jsonContent) {
        this.jsonContent = jsonContent;
    }
}
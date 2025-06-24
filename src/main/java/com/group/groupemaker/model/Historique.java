package com.group.groupemaker.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Historique {

    public Historique() {

    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateTirage;

    private String nomListe;

    @PrePersist
    protected void onCreate() {
        this.dateTirage = LocalDateTime.now();
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public LocalDateTime getDateTirage() {
        return dateTirage;
    }

    public String getNomListe() {
        return nomListe;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDateTirage(LocalDateTime dateTirage) {
        this.dateTirage = dateTirage;
    }

    public void setNomListe(String nomListe) {
        this.nomListe = nomListe;
    }
}

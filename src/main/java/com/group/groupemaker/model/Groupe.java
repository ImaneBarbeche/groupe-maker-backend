package com.group.groupemaker.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Groupe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-incrémentation de l'id par postgreSQL
    private Long id;

    private String nom;

    private String criteres;

    private LocalDateTime dateCreation;

    @OneToMany
    private List<Personne> personnes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "liste_id", nullable = false)
    private Liste liste;

    @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
    }

    public Groupe() {
    }

    public Groupe(String nom, String criteres, List<Personne> personnes) {
        this.nom = nom;
        this.criteres = criteres;
        this.personnes = personnes;

    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCriteres() {
        return criteres;
    }

    public void setCriteres(String criteres) {
        this.criteres = criteres;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Personne> getPersonnes() {
        return personnes;
    }

    public void setPersonnes(List<Personne> personnes) {
        this.personnes = personnes;
    }

    public Liste getListe() {
        return liste;
    }

    public void setListe(Liste liste) {
        this.liste = liste;
    }

}

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

    @ManyToMany
    private List<Personne> eleves = new ArrayList<>(); // Liste des élèves affectés à un groupe

    @ManyToOne
    @JoinColumn(name = "formateur_id") // facultatif mais clair
    private Formateur formateur;

    @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
    }

    public Groupe() {
    }

    public Groupe(String nom, String criteres, List<Personne> personnes, Formateur formateur) {
        this.nom = nom;
        this.criteres = criteres;
        this.personnes = personnes;
        this.formateur = formateur;

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

    public Formateur getFormateur() {
        return formateur;
    }

    public void setFormateur(Formateur formateur) {
        this.formateur = formateur;
    }

}

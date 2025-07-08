package com.group.groupemaker.dto;

import com.group.groupemaker.model.Genre;
import com.group.groupemaker.model.Profil;

// Classe DTO pour Personne
public class PersonneDTO {
    private Long id;
    private String nom;
    private Genre genre;
    private int aisanceFr;
    private boolean ancienDwwm;
    private int niveauTechnique;
    private Profil profil;
    private int age;
    private Long utilisateurId; // Ajout de l'attribut utilisateurId

    // Constructeur par défaut
    public PersonneDTO() {}

    // Constructeur avec tous les champs
    public PersonneDTO(Long id, String nom, Genre genre, int aisanceFr, boolean ancienDwwm,
                       int niveauTechnique, Profil profil, int age, Long utilisateurId) {
        this.id = id;
        this.nom = nom;
        this.genre = genre;
        this.aisanceFr = aisanceFr;
        this.ancienDwwm = ancienDwwm;
        this.niveauTechnique = niveauTechnique;
        this.profil = profil;
        this.age = age;
        this.utilisateurId = utilisateurId;
    }

    // Constructeur sans utilisateurId
    public PersonneDTO(Long id, String nom, Genre genre, int aisanceFr, boolean ancienDwwm,
                       int niveauTechnique, Profil profil, int age) {
        this.id = id;
        this.nom = nom;
        this.genre = genre;
        this.aisanceFr = aisanceFr;
        this.ancienDwwm = ancienDwwm;
        this.niveauTechnique = niveauTechnique;
        this.profil = profil;
        this.age = age;
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public int getAisanceFr() {
        return aisanceFr;
    }

    public void setAisanceFr(int aisanceFr) {
        this.aisanceFr = aisanceFr;
    }

    public boolean isAncienDwwm() {
        return ancienDwwm;
    }

    public void setAncienDwwm(boolean ancienDwwm) {
        this.ancienDwwm = ancienDwwm;
    }

    public int getNiveauTechnique() {
        return niveauTechnique;
    }

    public void setNiveauTechnique(int niveauTechnique) {
        this.niveauTechnique = niveauTechnique;
    }

    public Profil getProfil() {
        return profil;
    }

    public void setProfil(Profil profil) {
        this.profil = profil;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Long getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }
}
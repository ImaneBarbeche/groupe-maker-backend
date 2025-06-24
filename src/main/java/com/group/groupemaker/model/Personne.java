package com.group.groupemaker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Personne {

    public Personne() {
    }

    public Personne(String nom, String genre, Integer aisanceFr, Boolean ancienDwwm,
            Integer niveauTechnique, String profil, Integer age, Liste liste) {
        this.nom = nom;
        this.genre = genre;
        this.aisanceFr = aisanceFr;
        this.ancienDwwm = ancienDwwm;
        this.niveauTechnique = niveauTechnique;
        this.profil = profil;
        this.age = age;
        this.liste = liste;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    private String nom;

    @NotNull
    private String genre;

    @NotNull
    @Min(1)
    @Max(4)
    private Integer aisanceFr;

    @NotNull
    private Boolean ancienDwwm;

    @NotNull
    @Min(1)
    @Max(4)
    private Integer niveauTechnique;

    @NotNull
    private String profil;

    @NotNull
    @Min(18)
    @Max(99)
    private Integer age;

    @ManyToOne
    @JoinColumn(name = "liste_id") // facultatif mais plus clair
    private Liste liste;

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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getAisanceFr() {
        return aisanceFr;
    }

    public void setAisanceFr(Integer aisanceFr) {
        this.aisanceFr = aisanceFr;
    }

    public Boolean getAncienDwwm() {
        return ancienDwwm;
    }

    public void setAncienDwwm(Boolean ancienDwwm) {
        this.ancienDwwm = ancienDwwm;
    }

    public Integer getNiveauTechnique() {
        return niveauTechnique;
    }

    public void setNiveauTechnique(Integer niveauTechnique) {
        this.niveauTechnique = niveauTechnique;
    }

    public String getProfil() {
        return profil;
    }

    public void setProfil(String profil) {
        this.profil = profil;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Liste getListe() {
        return liste;
    }

    public void setListe(Liste liste) {
        this.liste = liste;
    }
}

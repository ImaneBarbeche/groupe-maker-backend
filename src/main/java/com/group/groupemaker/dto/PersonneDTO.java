package com.group.groupemaker.dto;

// Classe DTO pour Personne
public class PersonneDTO {
    private Long id;
    private String nom;
    private String genre;
    private int aisanceFr;
    private boolean ancienDwwm;
    private int niveauTechnique;
    private String profil;
    private int age;
    private Long listeId;

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

    public String getProfil() {
        return profil;
    }

    public void setProfil(String profil) {
        this.profil = profil;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Long getListeId() {
        return listeId;
    }

    public void setListeId(Long listeId) {
        this.listeId = listeId;
    }

    public PersonneDTO() {}

    public PersonneDTO(Long id, String nom, String genre, int aisanceFr, boolean ancienDwwm,
                       int niveauTechnique, String profil, int age, Long listeId) {
        this.id = id;
        this.nom = nom;
        this.genre = genre;
        this.aisanceFr = aisanceFr;
        this.ancienDwwm = ancienDwwm;
        this.niveauTechnique = niveauTechnique;
        this.profil = profil;
        this.age = age;
        this.listeId = listeId;
    }
}

package com.group.groupemaker.dto;

public class FormateurDTO {
    private String email;
    private String motDePasse;
    private String nom;
    private String prenom;
    private String expertise;

    // Getters
    public String getEmail() {
        return email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getExpertise() {
        return expertise;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }
}
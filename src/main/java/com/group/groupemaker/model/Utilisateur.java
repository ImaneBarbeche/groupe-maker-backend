package com.group.groupemaker.model;

import java.time.LocalDateTime;

import jakarta.persistence.*; // lie la classe à une table en base de données

@Entity
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String prenom;
    private String nom;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String motDePasse;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    @Column(nullable = false)
    private boolean active;

    private LocalDateTime dateAcceptationCGU;

    public Utilisateur() {}

    public Utilisateur(String prenom, String nom, String email, String motDePasse) {
    this.prenom = prenom;
    this.nom = nom;
    this.email = email;
    this.motDePasse = motDePasse;
    this.active = true;
    this.dateCreation = LocalDateTime.now();
}


    @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getDateAcceptationCGU() {
        return dateAcceptationCGU;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setDateAcceptationCGU(LocalDateTime dateAcceptationCGU) {
        this.dateAcceptationCGU = dateAcceptationCGU;
    }

    
}

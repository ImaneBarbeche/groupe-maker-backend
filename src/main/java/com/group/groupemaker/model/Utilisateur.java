package com.group.groupemaker.model;

import java.time.LocalDateTime;

import jakarta.persistence.*; // lie la classe à une table en base de données

@Entity // classe gérée comme une table
public class Utilisateur {
    @Id // clé primaire de la table
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-incrémentation de l'id par postgreSQL
    private Long id; // type recommandé pour les identifiants JPA

    // champs pour les colonnes de la table
    private String prenom;
    private String nom;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false) // rend le champ obligatoire
    private String motDePasse;
    @Column(nullable = false)
    private String role; // "USER" ou "FORMATEUR"

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    @Column(nullable = false)
    private boolean active;

    private LocalDateTime dateAcceptationCGU;
    
    // constructeur vide
    public Utilisateur() {
    }

    // constructeur pour créer un utilisateur
    public Utilisateur(String prenom, String nom, String email, String motDePasse, String role) {
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
    }

    // Initialise automatiquement la date de création à l'insertion
    @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
    }

    // getters et setters
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

    public String getRole() {
        return role;
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

    public void setRole(String role) {
        this.role = role;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setDateAcceptationCGU(LocalDateTime dateAcceptationCGU) {
        this.dateAcceptationCGU = dateAcceptationCGU;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

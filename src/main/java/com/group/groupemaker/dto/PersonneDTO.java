package com.group.groupemaker.dto;

// Classe DTO pour Personne
public class PersonneDTO {
    private Long id;
    private String nom;
    private String prenom;
    private int aisanceFr;
    
    public PersonneDTO() {}
    
    public PersonneDTO(Long id, String nom, String prenom, int aisanceFr) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.aisanceFr = aisanceFr;
    }
    
    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    
    public int getAisanceFr() { return aisanceFr; }
    public void setAisanceFr(int aisanceFr) { this.aisanceFr = aisanceFr; }
}


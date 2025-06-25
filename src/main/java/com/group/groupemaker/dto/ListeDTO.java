package com.group.groupemaker.dto;

public class ListeDTO {
    private Long id;
    private String nom;

    public ListeDTO() {}

    public ListeDTO(Long id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
}

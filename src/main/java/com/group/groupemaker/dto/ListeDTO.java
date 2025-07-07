package com.group.groupemaker.dto;

import java.util.List;

public class ListeDTO {
    private Long id;
    private String nom;
    private String description; // Ajout du champ description
    private List<PersonneDTO> personnes;

    // Constructeurs
    public ListeDTO() {}

    // Constructeur avec id et nom
    public ListeDTO(Long id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    // Constructeur avec id, nom et description
    public ListeDTO(Long id, String nom, String description) {
        this.id = id;
        this.nom = nom;
        this.description = description;
    }

    // Constructeur avec id, nom, description et personnes
    public ListeDTO(Long id, String nom, String description, List<PersonneDTO> personnes) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.personnes = personnes;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PersonneDTO> getPersonnes() {
        return personnes;
    }

    public void setPersonnes(List<PersonneDTO> personnes) {
        this.personnes = personnes;
    }
}
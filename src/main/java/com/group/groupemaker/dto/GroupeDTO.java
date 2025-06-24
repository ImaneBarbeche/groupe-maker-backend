package com.group.groupemaker.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Classe DTO pour Groupe
public class GroupeDTO {
    private String nom;
    private LocalDateTime dateCreation;
    private List<PersonneDTO> personnes;
    
    public GroupeDTO() {}
    
    public GroupeDTO(String nom, LocalDateTime dateCreation) {
        this.nom = nom;
        this.dateCreation = dateCreation;
        this.personnes = new ArrayList<>();
    }
    
    // Getters et setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    
    public List<PersonneDTO> getPersonnes() { return personnes; }
    public void setPersonnes(List<PersonneDTO> personnes) { this.personnes = personnes; }
}

package com.group.groupemaker.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GroupeDTO {
    private Long id;
    private String nom;
    private String criteres;
    private LocalDateTime dateCreation;
    private Long listeId;
    private List<PersonneDTO> personnes;

    public GroupeDTO() {
        this.personnes = new ArrayList<>();
    }

    public GroupeDTO(Long id, String nom, String criteres, LocalDateTime dateCreation, Long listeId) {
        this.id = id;
        this.nom = nom;
        this.criteres = criteres;
        this.dateCreation = dateCreation;
        this.listeId = listeId;
        this.personnes = new ArrayList<>();
    }

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getCriteres() { return criteres; }
    public void setCriteres(String criteres) { this.criteres = criteres; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public Long getListeId() { return listeId; }
    public void setListeId(Long listeId) { this.listeId = listeId; }

    public List<PersonneDTO> getPersonnes() { return personnes; }
    public void setPersonnes(List<PersonneDTO> personnes) { this.personnes = personnes; }
}
